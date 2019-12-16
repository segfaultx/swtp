package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.TradeOfferNotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import de.hsrm.mi.swtp.exchangeplatform.repository.UserRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.TimeslotRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.TradeOfferRepository;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TradeOfferService implements RestService<TradeOffer, Long> {
	
	// TODO: Prüfen ob Klasse vom Model Refactoring beeinflusst
	
	TradeOfferRepository tradeOfferRepository;
	TimeslotRepository timeSlotRepository;
	UserRepository userRepository;
	TradeFilter filterService;
	
	/**
	 * Method to get personalized {@link TradeOffer}s for a students timetable
	 *
	 * @param timeslots timetable of student
	 *
	 * @return map containing tradeoffers for each timeslot
	 */
	public Map<Timeslot, Map<String, List<TradeOffer>>> getTradeOffersForTimeSlots(List<Timeslot> timeslots) throws RuntimeException {
		log.info("Creating unfiltered Map of tradeoffers");
		Map<Timeslot, List<TradeOffer>> timeslotTradeOffers = new HashMap<>();
		timeslots.forEach(timeslot -> timeslotTradeOffers.put(timeslot, tradeOfferRepository.findAllBySeek(timeslot)));
		List<Filter> filters = new ArrayList<>();
		filters.add(new OfferFilter());
		filters.add(new CapacityFilter());
		filters.add(new CollisionFilter());
		return filterService.applyFilter(timeslotTradeOffers, filters);
	}
	
	/**
	 * Method to process a requested trade transaction, transaction info is gathered from {@link TradeOffer}'s id
	 *
	 * @param requesterId id of requester
	 * @param tradeId     accepted trade
	 *
	 * @return new Timeslot of requester
	 *
	 * @throws Exception if either requesterId or tradeId cant be found
	 */
	@Transactional
	public Timeslot tradeTimeslots(long requesterId, long tradeId) throws Exception {
		log.info(String.format("Performing Trade for requester: %d, trade: %d", requesterId, tradeId));
		var requester = userRepository.findById(requesterId)
				  			.orElseThrow(NotFoundException::new);
		var trade = tradeOfferRepository.findById(tradeId)
							.orElseThrow(NotFoundException::new);
		requester.getTimeslots().remove(trade.getSeek());
		requester.getTimeslots().add(trade.getOffer());
		var tradePartner = trade.getOfferer();
		tradePartner.getTimeslots().remove(trade.getOffer());
		tradePartner.getTimeslots().add(trade.getSeek());
		return trade.getOffer();
	}
	
	/**
	 * Method to delete a given {@link TradeOffer} by id of a student
	 *
	 * @param studentId delete requester's id
	 * @param tradeId   tradeId of item which is supposed to be deleted
	 *
	 * @return true if successful
	 *
	 * @throws RuntimeException if tradeoffer cannot be looked up or requester isnt owner of the requested trade
	 */
	public boolean deleteTradeOffer(long studentId, long tradeId) throws RuntimeException {
		log.info(String.format("Looking up Tradeoffer with id: %d. Requester: %d", tradeId, studentId));
		tradeOfferRepository.findById(tradeId).ifPresentOrElse(tradeOffer -> {
			if(tradeOffer.getOfferer().getStudentNumber() != studentId) {
				log.info(String.format("Error: Requester is not owner of trade with id: %d, requester: %d", tradeId, studentId));
				throw new RuntimeException("not your tradeoffer");
			}
			log.info(String.format("Successfully deleted tradeoffer with id: %d of student: %d", tradeId, studentId));
			tradeOfferRepository.delete(tradeOffer);
		}, NotFoundException::new);
		return true;
	}
	
	/**
	 * Method to create a tradeoffer of student requeste
	 *
	 * @param studentId requesters id
	 * @param offerId   requesters offered timeslot
	 * @param seekId    requesters sought timeslot
	 *
	 * @return new tradeoffer instance if successful
	 *
	 * @throws NotFoundException thrown if could not lookup any of the given id's
	 */
	public TradeOffer createTradeOffer(long studentId, long offerId, long seekId) throws NotFoundException {
		log.info(String.format("Creating new Tradeoffer for Student: %d with offer/request: %d/%d", studentId, offerId, seekId));
		TradeOffer tradeoffer = new TradeOffer();
		tradeoffer.setOfferer(userRepository.findById(studentId)
			.orElseThrow(NotFoundException::new));
		tradeoffer.setOffer(timeSlotRepository.findById(offerId).orElseThrow(NotFoundException::new));
		tradeoffer.setSeek(timeSlotRepository.findById(seekId).orElseThrow(NotFoundException::new));
		log.info(String.format("Successfully created new Tradeoffer for Student: %d with offer/seek: %d/%d", studentId, offerId, seekId));
		return tradeOfferRepository.save(tradeoffer);
	}
	
	/**
	 * Method to get all tradeoffers
	 *
	 * @return list of all tradeoffers
	 */
	@Override
	public List<TradeOffer> getAll() {
		return tradeOfferRepository.findAll();
	}
	
	/**
	 * Method to lookup a {@link TradeOffer} by id
	 *
	 * @param aLong id of item to lookup
	 *
	 * @return requested tradeoffer
	 *
	 * @throws NotFoundException if item cant be looked up
	 */
	@Override
	public TradeOffer getById(Long aLong) throws NotFoundException {
		return tradeOfferRepository.findById(aLong).orElseThrow(() -> new NotFoundException(aLong));
	}
	
	/**
	 * Method to save a given {@link TradeOffer}
	 *
	 * @param item item to be saved
	 *
	 * @throws IllegalArgumentException if item is malformed
	 */
	@Override
	public void save(TradeOffer item) throws IllegalArgumentException {
		var dbItem = tradeOfferRepository.save(item);
		log.info(String.format("Successfully saved Tradeoffer with ID: %d", dbItem.getId()));
	}
	
	/**
	 * Method to deleta a {@link TradeOffer} by id
	 *
	 * @param aLong id of item to delete
	 *
	 * @throws IllegalArgumentException if item couldnt be found
	 */
	@Override
	public void delete(Long aLong) throws IllegalArgumentException {
		tradeOfferRepository.delete(tradeOfferRepository.findById(aLong).orElseThrow());
	}
	
	/**
	 * Method to update a given {@link TradeOffer}
	 *
	 * @param aLong  item to lookup (update)
	 * @param update item with updated values
	 *
	 * @return true if successful
	 *
	 * @throws IllegalArgumentException if item couldnt be looked up
	 */
	@Override
	public boolean update(Long aLong, TradeOffer update) throws IllegalArgumentException {
		log.info(String.format("Updating TradeOffer: %d", aLong));
		var dbItem = tradeOfferRepository.findById(aLong).orElseThrow(() -> {
			log.info(String.format("ERROR Updating TradeOffer: %d", aLong));
			throw new IllegalArgumentException();
		});
		BeanUtils.copyProperties(update, dbItem, "id");
		log.info(String.format("Successfully updated Tradeoffer: %d", aLong));
		return true;
	}
}
