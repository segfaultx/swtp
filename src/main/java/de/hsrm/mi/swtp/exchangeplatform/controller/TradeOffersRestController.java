package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.TradeOfferNotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TimeTable;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import de.hsrm.mi.swtp.exchangeplatform.model.rest.TradeRequest;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.TradeOfferService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/trades")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TradeOffersRestController {
	
	TradeOfferService tradeOfferService;
	
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
	@ApiOperation(value = "Delete tradeoffer of student", nickname = "deleteTradeOfferOfStudent")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "successfully deleted tradeoffer"),
							@ApiResponse(code = 403, message = "unauthorized delete attempt"),
							@ApiResponse(code = 404, message = "tradeoffer not found") })
	public ResponseEntity deleteTradeOffer(@ApiParam(value = "Numeric ID of the student", required = true) @PathVariable("studentId") long studentId,
										   @ApiParam(value = "Numeric ID of the tradeoffer", required = true) @PathVariable("tradeId") long tradeId
										  ) {
		log.info(String.format("DELETE Request Student: %d TradeOffer: %d", studentId, tradeId));
		try {
			if(tradeOfferService.deleteTradeOffer(studentId, tradeId)) {
				log.info(String.format("DELETE Request successful Student: %d TradeOffer: %d", studentId, tradeId));
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch(TradeOfferNotFoundException ex) {
			log.info(String.format("ERROR while DELETE Request Student: %d TradeOffer: %d - Entity not found", studentId, tradeId));
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
	@ApiOperation(value = "create tradeoffer for student", nickname = "createTradeOfferForStudent")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "successfully deleted tradeoffer"),
							@ApiResponse(code = 403, message = "unauthorized delete attempt"),
							@ApiResponse(code = 404, message = "tradeoffer not found") })
	public ResponseEntity<TradeOffer> createTradeOffer(
			@ApiParam(value = "Numeric ID of the student", required = true) @PathVariable("studentId") long studentId,
			@ApiParam(value = "Numeric ID of the offered timeslot", required = true) @PathVariable("offer") long offerId,
			@ApiParam(value = "Numeric ID of the wanted timeslot", required = true) @PathVariable("seek") long seekId
													  ) {
		log.info(String.format("POST Request Student: %d with Offer/Seek: %d/%d", studentId, offerId, seekId));
		try {
			var tradeoffer = tradeOfferService.createTradeOffer(studentId, offerId, seekId);
			log.info(String.format("SUCCESS POST Request Student: %d with Offer/Seek: %d/%d - insert successful", studentId, offerId, seekId));
			return new ResponseEntity<>(tradeoffer, HttpStatus.OK);
		} catch(NotFoundException ex) {
			log.info(String.format("ERROR POST Request Student: %d with Offer/Seek: %d/%d - error while creating entity", studentId, offerId, seekId));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * POST request handler.
	 * proviced an endpoint to {@code '/api/v1/trades} for users to create a {@link TradeOffer}
	 *
	 * @param tradeRequest tradeoffer to create
	 *
	 * @return {@link HttpStatus#OK} if successful, {@link HttpStatus#BAD_REQUEST} if tradeoffer is malformed
	 */
	
	@PostMapping("/create")
	@ApiOperation(value = "create tradeoffer for student", nickname = "createTradeOfferForStudent")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "successfully created tradeoffer"),
							@ApiResponse(code = 403, message = "unauthorized create attempt"),
							@ApiResponse(code = 400, message = "malformed trade request") })
	public ResponseEntity<TradeOffer> createTradeOffer(
			@ApiParam(value = "Object containing ID's of student, wanted and offered timeslot", required = true) @Valid TradeRequest tradeRequest,
			BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			log.info("Malformed traderequest");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		log.info(String.format("POST Request create new traderequest: Requester: %d Offered: %d, Seek: %d",
							   tradeRequest.getOfferedByStudentMatriculationNumber(), tradeRequest.getOfferedTimeslotId(), tradeRequest.getWantedTimeslotId()
							  ));
		var persistedTradeOffer = tradeOfferService.createTradeOffer(tradeRequest.getOfferedByStudentMatriculationNumber(), tradeRequest.getOfferedTimeslotId(),
																	 tradeRequest.getWantedTimeslotId()
																	);
		log.info(String.format("POST Request successful: created new tradeoffer with id: %d requester: %d", persistedTradeOffer.getId(),
							   persistedTradeOffer.getOfferer().getStudentId()
							  ));
		return new ResponseEntity<>(persistedTradeOffer, HttpStatus.OK);
	}
	
	@PostMapping
	@ApiOperation(value = "request trade", nickname = "requestTrade")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "successfully processed traderequest"),
							@ApiResponse(code = 403, message = "unauthorized trade attempt"),
							@ApiResponse(code = 400, message = "malformed trade request") })
	public ResponseEntity<TimeTable> requestTrade(@Valid TradeRequest tradeRequest) {
		log.info(String.format("Traderequest of student: %d for timeslot: %d, offer: %d", tradeRequest.getOfferedByStudentMatriculationNumber(),
							   tradeRequest.getOfferedTimeslotId(), tradeRequest.getWantedTimeslotId()
							  ));
		var timetable = tradeOfferService.tradeTimeslots(tradeRequest.getOfferedByStudentMatriculationNumber(), tradeRequest.getOfferedTimeslotId(),
														 tradeRequest.getWantedTimeslotId()
														);
		return new ResponseEntity<>(timetable, HttpStatus.OK);
	}
}
