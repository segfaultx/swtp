package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.messaging.sender.PersonalMessageSender;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.TypeOfTimeslots;
import de.hsrm.mi.swtp.exchangeplatform.repository.ModuleRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.TimeslotRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.TradeOfferRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.UserRepository;
import de.hsrm.mi.swtp.exchangeplatform.service.admin.AdminTradeService;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.Filter;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.TradeFilter.CapacityFilter;
import de.hsrm.mi.swtp.exchangeplatform.service.settings.AdminSettingsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TradeOfferService implements RestService<TradeOffer, Long> {
	
	PersonalMessageSender personalMessageSender;
	TradeOfferRepository tradeOfferRepository;
	TimeslotRepository timeSlotRepository;
	UserRepository userRepository;
	//Tradeilter filterService; //TODO: wait for filter fix
	AdminTradeService adminTradeService;
	AdminSettingsService adminSettingsService;
	TradeService tradeService;
	ModuleRepository moduleRepository;
	
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
		filters.addAll(adminSettingsService.getAdminSettings().getCurrentActiveFilters());
		return null;//filterService.applyFilter(timeslotTradeOffers, filters); //TODO: use fixed filter function
	}
	
	/**
	 * Method to provide admins a forced trade / assignment of timeslot to a given student
	 *
	 * @param studentId        student to assign new {@link Timeslot}
	 * @param ownedTimeslotId  {@link Timeslot} to remove
	 * @param futureTimeslotId new {@link Timeslot} to assign to {@link de.hsrm.mi.swtp.exchangeplatform.model.data.User}
	 * @param adminId          id of admin
	 *
	 * @return new timeslot if successful
	 *
	 * @throws NotFoundException if any of the given ids couldnt be looked up
	 */
	public Timeslot adminTrade(long studentId, long ownedTimeslotId, long futureTimeslotId, long adminId) throws Exception {
		log.info(String.format("Starting forced trade (ADMIN: %d), student: %d, ownedTimeslot: %d, futureTimeslot: %d", adminId, studentId, ownedTimeslotId,
							   futureTimeslotId
							  ));
		return adminTradeService.assignTimeslotToStudent(studentId, ownedTimeslotId, futureTimeslotId, adminId);
	}
	
	/**
	 * Method to lookup potential tradeoffers for a given timeslot
	 *
	 * @param id timeslotid
	 *
	 * @return map with keys "instant", "trades" and "remaining" holding lists of timeslots
	 *
	 * @throws Exception if lookup fails
	 */
	public Map<String, List<Timeslot>> getTradeOffersForModule(long id, User user) throws Exception {
		Map<String, List<Timeslot>> out = new HashMap<>();
		List<Timeslot> instantTrades = new ArrayList<>();
		List<Timeslot> regularTrades = new ArrayList<>();
		List<Timeslot> remaining = new ArrayList<>();
		List<Timeslot> ownOffers = new ArrayList<>();
		var offeredTimeslot = timeSlotRepository.findById(id).orElseThrow();
		var trades = tradeOfferRepository.findAllBySeek(offeredTimeslot);
		trades.forEach(trade -> {
			if(trade.getId() != id) {
				if(trade.isInstantTrade()) instantTrades.add(trade.getOffer());
				else regularTrades.add(trade.getOffer());
			}
			
		});
		for(TradeOffer to : tradeOfferRepository.findAllByOffererAndOffer(user, offeredTimeslot)) {
			if(!ownOffers.contains(to.getSeek())) ownOffers.add(to.getSeek());
		}
		var allTimeslots = timeSlotRepository.findAllByModule(offeredTimeslot.getModule());
		allTimeslots.forEach(timeslot -> {
			if(timeslot.getId() != id && timeslot.getTimeSlotType() != TypeOfTimeslots.VORLESUNG && !ownOffers.contains(timeslot)) {
				if(!instantTrades.contains(timeslot) && !regularTrades.contains(timeslot)) remaining.add(timeslot);
			}
		});
		out.put("instant", instantTrades);
		out.put("trades", regularTrades);
		out.put("remaining", remaining);
		out.put("ownOffers", ownOffers);
		return out;
	}
	
	/**
	 * Method to process a requested trade transaction, transaction info is gathered from {@link TradeOffer}'s id
	 *
	 * @param requestingUser requesting User
	 * @param offeredTimeslot Timeslot that's been offered for trade
	 * @param requestedTimeslot Timeslot that's been requested for trade
	 *
	 * @return new Timeslot of requesting User
	 *
	 * @throws RuntimeException if either requesterId or tradeId cant be found
	 */
	@Transactional
	public Timeslot tradeTimeslots(User requestingUser, Timeslot offeredTimeslot, Timeslot requestedTimeslot) throws Exception {
		
		TradeOffer tradeOffer = findFinalTradeOffer(offeredTimeslot, requestedTimeslot);
		
		// TODO: handle null
		if (tradeOffer == null) {
			throw new Exception("No final TradeOffer found");
		}
		
		User offereringUser = tradeOffer.getOfferer();
		
		log.info("Performing Trade: From {} to {} with Timeslots {} and {}",
				 offereringUser, requestingUser, offeredTimeslot, requestedTimeslot);
		
		if(tradeService.doTrade(offereringUser, requestingUser, offeredTimeslot, requestedTimeslot)) {
			deleteTradeOffer(tradeOffer);
			return timeSlotRepository.findById(requestedTimeslot.getId()).orElseThrow();
		}
		throw new RuntimeException();
	}
	
	public TradeOffer findFinalTradeOffer(Timeslot offeredTimeslot, Timeslot requestedTimeslot) {
		List<TradeOffer> tradeOffers;
		Random random = new Random();
		
		// Fetch a  List of all tradeoffers
		tradeOffers = tradeOfferRepository.findAll();
		
		// Remove all tradeOffers where requested is not offered and offered is not seek
		tradeOffers.removeIf(tradeOffer ->
									 !(tradeOffer.getOffer() == requestedTimeslot && tradeOffer.getSeek() == offeredTimeslot));
		
		// filter the list according to active filters
		// TODO: iterate through filters and apply them
		Filter capacityFilter = new CapacityFilter();

		tradeOffers = capacityFilter.filter(tradeOffers);
		// if no matching TradeOffer was found return null
		if(tradeOffers.size() == 0) return null;
		
		// if more than one tradeoffer remains, then spit out a random one
		if(tradeOffers.size() > 1) {
			return tradeOffers.get(random.nextInt(tradeOffers.size() - 1));
		} else {
			return tradeOffers.get(0);
		}
	}
	
	/**
	 * Method to delete a given {@link TradeOffer}
	 *
	 * @param tradeOffer delete given TradeOffer from Database
	 *
	 * @return true if successful
	 *
	 */
	public boolean deleteTradeOffer(TradeOffer tradeOffer) {
		if(tradeOffer == null) return false;
		log.info("Successfully deleted tradeoffer with ID: {} of student: %d", tradeOffer.getId());
		tradeOfferRepository.delete(tradeOffer);
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
		tradeoffer.setOfferer(userRepository.findById(studentId).orElseThrow(() -> {
			log.info(String.format("ERROR while creating Tradeoffer: StudentId not found: %d", studentId));
			return new NotFoundException(studentId);
		}));
		tradeoffer.setOffer(timeSlotRepository.findById(offerId).orElseThrow(() -> {
			log.info(String.format("ERROR while creating Tradeoffer: offerId not found: %d", offerId));
			return new NotFoundException(offerId);
		}));
		tradeoffer.setSeek(timeSlotRepository.findById(seekId).orElseThrow(() -> {
			log.info(String.format("ERROR while creating Tradeoffer: seekId not found: %d", seekId));
			return new NotFoundException(seekId);
		}));
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
	
	public List<TradeOffer> getAllTradeoffersForStudent(User user) {
		
		return tradeOfferRepository.findAllByOfferer(user);
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
