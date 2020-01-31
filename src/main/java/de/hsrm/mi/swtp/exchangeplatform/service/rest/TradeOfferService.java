package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager.TimeslotTopicManager;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.TradeOfferSuccessfulMessage;
import de.hsrm.mi.swtp.exchangeplatform.messaging.sender.PersonalMessageSender;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.TypeOfTimeslots;
import de.hsrm.mi.swtp.exchangeplatform.model.rest.TradeWrapper;
import de.hsrm.mi.swtp.exchangeplatform.repository.TimeslotRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.TradeOfferRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.UserRepository;
import de.hsrm.mi.swtp.exchangeplatform.service.admin.AdminTradeService;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.utils.FilterUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.reflect.InvocationTargetException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TradeOfferService implements RestService<TradeOffer, Long> {
	
	TradeOfferRepository tradeOfferRepository;
	TimeslotRepository timeSlotRepository;
	UserRepository userRepository;
	AdminTradeService adminTradeService;
	TradeService tradeService;
	PersonalMessageSender personalMessageSender;
	TimeslotTopicManager timeslotTopicManager;
	
	FilterUtils filterUtils;

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
	@Transactional
	public List<TradeWrapper> getTradeOffersForModule(long id, User user) throws Exception {
		List<TradeWrapper> out = new ArrayList<>();
		
		var offeredTimeslot = timeSlotRepository.findById(id).orElseThrow();
		
		// Lookup all trades which want the offered timeslot
		var trades = tradeOfferRepository.findAllBySeek(offeredTimeslot);
		var ownOffers = tradeOfferRepository.findAllByOffererAndOffer(user, offeredTimeslot);
		
		// merge own offers + trades to check which timeslots are left (no tradeoffers)
		List<TradeOffer> mergedOffers = new ArrayList<>();
		mergedOffers.addAll(trades);
		mergedOffers.addAll(ownOffers);
		
		// fetch all timeslots of module
		var allTimeslots = timeSlotRepository.findAllByModule(offeredTimeslot.getModule());
		
		// lookup all added timeslots
		var allAddedTimeslots = mergedOffers.stream()
				.map(TradeOffer::getOffer)
				.collect(toList());
		
		var ownOfferedTimeslots = ownOffers
									.stream().map(TradeOffer::getOffer).collect(toList())
									.stream().map(Timeslot::getId).collect(toList());
		// reduce all timeslots to a list only containing timeslots not contained in allAddedTimeslots which are
		// also not of type VORLESUNG, also remove the requested timeslot
		var remainingTimeslots = allTimeslots
				.stream()
				.filter(item -> !allAddedTimeslots.contains(item) &&
						item.getTimeSlotType() != TypeOfTimeslots.VORLESUNG
					   && !item.getId().equals(id)
					   && !ownOfferedTimeslots.contains(item.getId()))
				.collect(toList());
		
		// create fake tradeoffers and add them to trades (to check wether they collide with the students timetable)
		List<TradeOffer> fakeTradeOffers = new ArrayList<>();
		remainingTimeslots.forEach(ts -> {
			TradeOffer fake = new TradeOffer();
			fake.setSeek(offeredTimeslot);
			fake.setOffer(ts);
			fakeTradeOffers.add(fake);
		});
		trades.addAll(fakeTradeOffers);
		
		// filter out all trades which collide with timetable or requester (PRACTICAL)
		var nonCollidingTradesPractical = filterUtils
				.getFilterByName("CollisionFilter")
				.doFilter(trades, user);
		
		// add all practical colliding tradeoffers to tradewrapper list
		
		List.copyOf(trades)
				.stream()
				.filter(item -> !nonCollidingTradesPractical.contains(item))
				.collect(toList())
				.forEach(trade -> {
					TradeWrapper toAdd = new TradeWrapper();
					toAdd.setTimeslot(trade.getOffer());
					toAdd.setCollision(true);
					out.add(toAdd);
					trades.remove(trade);
				});
		
		// filter out all trades which collide with timetable or requester (LECTURE)
		var nonCollidingTradesLecture  = filterUtils.getFilterByName("LectureCollisionFilter")
				.doFilter(trades, user);
		List.copyOf(trades)
			.stream()
			.filter(item -> !nonCollidingTradesLecture.contains(item))
			.collect(Collectors.toList())
			.forEach(trade -> {
				TradeWrapper toAdd = new TradeWrapper();
				toAdd.setTimeslot(trade.getOffer());
				toAdd.setCollisionLecture(true);
				out.add(toAdd);
				trades.remove(trade);
			});
		
		// lookup own tradeoffers -> add them with own flag set
		
		ownOffers.forEach(item -> {
			TradeWrapper toAdd = new TradeWrapper();
			toAdd.setTimeslot(item.getSeek());
			toAdd.setOwnOffer(true);
			out.add(toAdd);
		});
		
		// all collisions have been removed, add them as either regular or instant trades to out
		trades.forEach(item -> {
			TradeWrapper toAdd = new TradeWrapper();
			toAdd.setTimeslot(item.getOffer());
			
			if (item.getId() == null){
				toAdd.setRemaining(true);
			}
			else {
				toAdd.setInstantTrade(item.isInstantTrade());
				toAdd.setTrade(!item.isInstantTrade());
			}
			out.add(toAdd);
		});
		
		
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
	public Timeslot tradeTimeslots(User requestingUser, Timeslot offeredTimeslot, Timeslot requestedTimeslot, User seeker) throws Exception {
		
		TradeOffer tradeOffer = findFinalTradeOffer(offeredTimeslot, requestedTimeslot, seeker);
		
		// TODO: handle null
		if (tradeOffer == null) {
			throw new Exception("No final TradeOffer found");
		}
		
		final User offereringUser = tradeOffer.getOfferer();
		
		log.info("Performing Trade: From {} to {} with Timeslots {} and {}",
				 offereringUser, requestingUser, offeredTimeslot, requestedTimeslot);
		
		if(tradeService.doTrade(offereringUser, requestingUser, offeredTimeslot, requestedTimeslot)) {
			deleteTradeOffer(tradeOffer);
			
			// notify user who requests to trade with offerer
			personalMessageSender.send(requestingUser.getId(),
									   TradeOfferSuccessfulMessage.builder().newTimeslot(offeredTimeslot)
																  .oldTimeslotId(offeredTimeslot.getId())
																  .topic(timeslotTopicManager.getTopic(requestedTimeslot))
																  .build()
									  );
			
			// notify offerer that his/her trade was resolved
			personalMessageSender.send(offereringUser.getId(),
									   TradeOfferSuccessfulMessage.builder()
									   							.newTimeslot(offeredTimeslot)
																  .oldTimeslotId(requestedTimeslot.getId())
																  .topic(timeslotTopicManager.getTopic(offeredTimeslot))
																  .build()
									  );
			
			
			log.info("TRADING...");
			log.info("┌─ TradeOfferSuccessfulMessage: SEND TO REQUESTING USER " + requestingUser.getAuthenticationInformation().getUsername());
			log.info(String.format("├─→ Timeslot %s ↔ Timeslot %s", requestedTimeslot.getId(), offeredTimeslot.getId()));
			log.info("└─ TradeOfferSuccessfulMessage: SEND TO OFFERING USER " + offereringUser.getAuthenticationInformation().getUsername());
			
			
			return timeSlotRepository.findById(requestedTimeslot.getId()).orElseThrow();
		}
		throw new RuntimeException();
	}
	
	/**
	 * Finds matching Tradeoffers for the offered and requested timeslots.
	 * Filters those TradeOffers and returns a random one from the remaining.
	 * @param offeredTimeslot {@link Timeslot} that's offered from active Tradeoffers
	 * @param requestedTimeslot  {@link Timeslot} that's been requested by Principal
	 * @return single TradeOffer that can be traded, or null if none are present or Filters killed all possible matching TradeOffers
	 */
	public TradeOffer findFinalTradeOffer(Timeslot offeredTimeslot, Timeslot requestedTimeslot, User seeker) {
		List<TradeOffer> tradeOffers;
		Random random = new Random();
		
		// Fetch a  List of all tradeoffers
		tradeOffers = tradeOfferRepository.findAll();
		
		// Remove all tradeOffers where requested is not offered and offered is not seek
		tradeOffers.removeIf(tradeOffer ->
									 !(tradeOffer.getOffer() == requestedTimeslot && tradeOffer.getSeek() == offeredTimeslot));
		
		// filter the list according to active filters
			tradeOffers = filterUtils.getFilteredTradeOffers(tradeOffers, seeker);
		
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
	
}
