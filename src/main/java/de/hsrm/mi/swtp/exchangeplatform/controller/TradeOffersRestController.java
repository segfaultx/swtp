package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.TradeOfferNotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import de.hsrm.mi.swtp.exchangeplatform.model.rest_models.*;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.TradeOfferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/trades")
@Slf4j
public class TradeOffersRestController {
	
	@Autowired
	private TradeOfferService tradeOfferService;
	
	/**
	 * DELETE request handler.
	 * provides an endpoint to {@code '/api/v1/trades/<id>/<id>'} through which an {student} may delete his {@link TradeOffer}.
	 *
	 * @param studentId studentId of requester
	 * @param tradeId   tradeId of tradeoffer which is to be deleted.
	 *
	 * @return {@link HttpStatus#OK} if tradeoffer was deleted, {@link HttpStatus#NOT_FOUND} if tradeoffer wasnt found,
	 * {@link HttpStatus#FORBIDDEN} if requester isnt owner of the tradeoffer.
	 */
	@DeleteMapping("/{studentId}/{tradeId}")
	public ResponseEntity deleteTradeOffer(@PathVariable("studentId") long studentId, @PathVariable("tradeId") long tradeId) {
		log.info(String.format("DELETE Request Student: %d TradeOffer: %d", studentId, tradeId));
		if(tradeOfferService.deleteTradeOffer(studentId, tradeId)) {
			log.info(String.format("DELETE Request successful Student: %d TradeOffer: %d", studentId, tradeId));
			return new ResponseEntity<>(HttpStatus.OK);
		}
		log.info(String.format("ERROR while DELETE Request Student: %d TradeOffer: %d - Student isn't owner of entity", studentId, tradeId));
		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}
	
	/**
	 * POST request handler.
	 * provides an endpoint to {@code '/api/v1/trades/<id>/<id>/<id>} through which an student may insert a new {@link TradeOffer}
	 *
	 * @param studentId id of requester
	 * @param offerId   id of timeslot which requester is offering
	 * @param seekId    id of sought timeslot
	 *
	 * @return {@link HttpStatus#OK} plus the new {@link TradeOffer} instance which has been created,
	 * {@link HttpStatus#INTERNAL_SERVER_ERROR} if the server encountered an error.
	 */
	@PostMapping("/{studentId}/{offer}/{seek}")
	public ResponseEntity<de.hsrm.mi.swtp.exchangeplatform.model.rest_models.TradeOffer> createTradeOffer(@PathVariable("studentId") long studentId,
																										  @PathVariable("offer") long offerId,
																										  @PathVariable("seek") long seekId
																										 ) {
		log.info(String.format("POST Request Student: %d with Offer/Seek: %d/%d", studentId, offerId, seekId));
		try {
			var tradeoffer = tradeOfferService.createTradeOffer(studentId, offerId, seekId);
			de.hsrm.mi.swtp.exchangeplatform.model.rest_models.TradeOffer restAnswer = new de.hsrm.mi.swtp.exchangeplatform.model.rest_models.TradeOffer();
			restAnswer.setId(tradeoffer.getId().intValue());
			restAnswer.setOfferedTimeslotId(tradeoffer.getOffer().getId());
			restAnswer.setWantedTimeslotId(tradeoffer.getSeek().getId());
			log.info(String.format("SUCCESS POST Request Student: %d with Offer/Seek: %d/%d - insert successful", studentId, offerId, seekId));
			return new ResponseEntity<>(restAnswer, HttpStatus.OK);
		} catch(NotFoundException ex) {
			log.info(String.format("ERROR POST Request Student: %d with Offer/Seek: %d/%d - error while creating entity", studentId, offerId, seekId));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * POST request handler.
	 * provides an endpoint to {@code '/api/v1/trades/<id>/<id>/accept} through which an student may accept a given {@link TradeOffer}
	 *
	 * @param studentId id of requester
	 * @param tradeId   id of trade which requester wants to accept
	 *
	 * @return {@link HttpStatus#OK} if successful, {@link HttpStatus#BAD_REQUEST} if trade transaction failed,
	 * {@link HttpStatus#INTERNAL_SERVER_ERROR} if an runtime exception was thrown by the service
	 */
	@PostMapping("/{studentId}/{tradeId}/accept")
	public ResponseEntity<Timeslot> acceptTrade(@PathVariable("studentId") long studentId, @PathVariable("tradeId") long tradeId,
												@RequestBody TradeRequest tradeRequest, BindingResult result
											   ) {
		
		log.info(String.format("POST Request Accept Trade: %d Requester: %d", tradeId, studentId));
		if(result.hasErrors()) {
			log.info(String.format("Post Request error, trade: %d from requester: %d failed", tradeId, studentId));
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		try {
			var answer = tradeOfferService.tradeTimeslots(studentId, tradeId);
			
			if(answer != null) {
				log.info(String.format("POST Request successful, performed trade: %d Requester: %d", tradeId, studentId));
				de.hsrm.mi.swtp.exchangeplatform.model.rest_models.Timeslot restAnswer = new de.hsrm.mi.swtp.exchangeplatform.model.rest_models.Timeslot();
				restAnswer.setId(answer.getId());
				//bereits gefixt auf aktuellem dev stand
				return new ResponseEntity<>(restAnswer, HttpStatus.OK);
			}
			log.info(String.format("Post Request error, trade: %d from requester: %d failed", tradeId, studentId));
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch(RuntimeException ex) {
			log.info(String.format("POST Request error, trade: %d from requester: %d - internal error", tradeId, studentId));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	public ResponseEntity<de.hsrm.mi.swtp.exchangeplatform.model.rest_models.TradeOffer> acceptTradeOffer(Long tradeId, Long studentId) {
		return null;
	}
	
	/**
	 * POST request handler.
	 * proviced an endpoint to {@code '/api/v1/trades} for users to create a {@link TradeOffer}
	 *
	 * @param tradeRequest tradeoffer to create
	 *
	 * @return {@link HttpStatus#OK} if successful, {@link HttpStatus#BAD_REQUEST} if tradeoffer is malformed
	 */
	public ResponseEntity<de.hsrm.mi.swtp.exchangeplatform.model.rest_models.TradeOffer> createTradeOffer(@Valid TradeRequest tradeRequest) throws
			NotFoundException {
		log.info(String.format("POST Request create new traderequest: Requester: %d Offered: %d, Seek: %d",
							   tradeRequest.getOfferedByStudentMatriculationNumber(), tradeRequest.getOfferedTimeslotId().get(),
							   tradeRequest.getWantedTimeslotId().get()
							  ));
		var persistedTradeOffer = tradeOfferService.createTradeOffer(tradeRequest.getOfferedByStudentMatriculationNumber(),
																	 tradeRequest.getOfferedTimeslotId().get(), tradeRequest.getWantedTimeslotId().get());
		de.hsrm.mi.swtp.exchangeplatform.model.rest_models.TradeOffer restAnswer = new de.hsrm.mi.swtp.exchangeplatform.model.rest_models.TradeOffer();
		restAnswer.setWantedTimeslotId(persistedTradeOffer.getSeek().getId());
		restAnswer.setOfferedTimeslotId(persistedTradeOffer.getOffer().getId());
		restAnswer.setId(persistedTradeOffer.getId().intValue());
		log.info(String.format("POST Request successful: created new tradeoffer with id: %d requester: %d", persistedTradeOffer.getId(),
							   persistedTradeOffer.getOfferer().getStudentNumber()
							  ));
		return new ResponseEntity<>(restAnswer, HttpStatus.OK);
	}
}
