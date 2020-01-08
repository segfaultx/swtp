package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TimeTable;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.model.rest.TradeRequest;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.TradeOfferService;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.UserService;
import de.hsrm.mi.swtp.exchangeplatform.service.settings.AdminSettingsService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/trades")
@Slf4j
@SecurityRequirement(name = "Authorization")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TradeOffersRestController {
	
	TradeOfferService tradeOfferService;
	UserService userService;
	AdminSettingsService adminSettingsService;
	
	/**
	 * DELETE request handler.
	 * provides an endpoint to {@code '/api/v1/trades/<id>/<id>'} through which an {student} may delete his {@link TradeOffer}.
	 *
	 * @param studentId studentId of requester
	 * @param seekId   tradeId of tradeoffer which is to be deleted.
	 *
	 * @return {@link HttpStatus#OK} if tradeoffer was deleted, {@link HttpStatus#NOT_FOUND} if tradeoffer wasnt found,
	 * {@link HttpStatus#FORBIDDEN} if requester isnt owner of the tradeoffer.
	 */
	@DeleteMapping("/{studentId}/{seekId}")
	@ApiOperation(value = "Delete tradeoffer with seekId of student", nickname = "deleteTradeOfferOfStudent")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "successfully deleted tradeoffer"),
							@ApiResponse(code = 403, message = "unauthorized delete attempt"),
							@ApiResponse(code = 404, message = "tradeoffer not found") })
	@PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
	public ResponseEntity deleteTradeOffer(@ApiParam(value = "Numeric ID of the student", required = true) @PathVariable("studentId") long studentId,
										   @ApiParam(value = "Numeric ID of the tradeoffer", required = true) @PathVariable("seekId") long seekId
										  ) throws Exception {
		if(adminSettingsService.isTradesActive()) {
			log.info(String.format("DELETE Request Student: %d TradeOffer: %d", studentId, seekId));
			if(tradeOfferService.deleteTradeOffer(studentId, seekId)) {
				log.info(String.format("DELETE Request successful Student: %d TradeOffer: %d", studentId, seekId));
				return new ResponseEntity<>(HttpStatus.OK);
			}
			log.info(String.format("ERROR while DELETE Request Student: %d TradeOffer: %d - Student isn't owner of entity", studentId, seekId));
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		} else return new ResponseEntity(HttpStatus.SERVICE_UNAVAILABLE);
		
	}
	
	/**
	 * POST request handler.
	 * proviced an endpoint to {@code '/api/v1/trades'} for users to create a {@link TradeOffer}
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
	@PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
	public ResponseEntity<TradeOffer> createTradeOffer(
			@ApiParam(value = "Object containing ID's of student, wanted and offered timeslot", required = true) @Valid @RequestBody TradeRequest tradeRequest,
			BindingResult bindingResult
													  ) throws NotFoundException {
		if(adminSettingsService.isTradesActive()) {
			if(bindingResult.hasErrors()) {
				log.info("Malformed traderequest");
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			log.info(String.format("POST Request create new traderequest: Requester: %d Offered: %d, Seek: %d",
								   tradeRequest.getOfferedByStudentMatriculationNumber(), tradeRequest.getOfferedTimeslotId(),
								   tradeRequest.getWantedTimeslotId()
								  ));
			var persistedTradeOffer = tradeOfferService.createTradeOffer(tradeRequest.getOfferedByStudentMatriculationNumber(),
																		 tradeRequest.getOfferedTimeslotId(), tradeRequest.getWantedTimeslotId()
																		);
			log.info(String.format("POST Request successful: created new tradeoffer with id: %d requester: %d", persistedTradeOffer.getId(),
								   persistedTradeOffer.getOfferer().getStudentNumber()
								  ));
			return new ResponseEntity<>(persistedTradeOffer, HttpStatus.OK);
		} else return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
	}
	
	/**
	 * POST request handler
	 * <p>
	 * provides an endpoint to {@code '/api/v1/trades'} for users to request a trade
	 *
	 * @param tradeRequest {@link TradeRequest} object containing requesters ID, offered Id and requested ID
	 *
	 * @return new timetable if trade was successful
	 */
	@PostMapping
	@ApiOperation(value = "request trade", nickname = "requestTrade")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "successfully processed traderequest"),
							@ApiResponse(code = 403, message = "unauthorized trade attempt"),
							@ApiResponse(code = 400, message = "malformed trade request") })
	@PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
	public ResponseEntity<TimeTable> requestTrade(@Valid @RequestBody TradeRequest tradeRequest) throws Exception {
		if(adminSettingsService.isTradesActive()) {
			log.info(String.format("Traderequest of student: %d for timeslot: %d, offer: %d", tradeRequest.getOfferedByStudentMatriculationNumber(),
								   tradeRequest.getOfferedTimeslotId(), tradeRequest.getWantedTimeslotId()
								  ));
			var timeslot = tradeOfferService.tradeTimeslots(tradeRequest.getOfferedByStudentMatriculationNumber(), tradeRequest.getOfferedTimeslotId(),
															tradeRequest.getWantedTimeslotId()
														   );
			TimeTable timetable = new TimeTable();
			timetable.setId(tradeRequest.getOfferedByStudentMatriculationNumber());
			timetable.setDateEnd(LocalDate.now()); // DIRTY QUICK FIX
			timetable.setDateStart(LocalDate.now());// DIRTY QUICK FIX
			User user = userService.getById(tradeRequest.getOfferedByStudentMatriculationNumber()).orElseThrow(NotFoundException::new);
			
			timetable.setTimeslots(user.getTimeslots());
			return new ResponseEntity<>(timetable, HttpStatus.OK);
		} else return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
	}
	
	/**
	 * GET request handler
	 * <p>
	 * provides an endpoint to {@link User}s to receive tradeoffers for a given timeslot
	 *
	 * @param id id of timeslot
	 *
	 * @return map with keys "instant", "trades", "remaining" holding lists of respective timeslots
	 *
	 * @throws Exception if lookups fail
	 */
	@GetMapping("/{id}")
	@ApiOperation(value = "request tradeOffers", nickname = "getTradesForTimeslot")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "successfully retrieved tradeoffers"),
							@ApiResponse(code = 403, message = "unauthorized tradeOffer request"),
							@ApiResponse(code = 400, message = "malformed tradeOffers request") })
	@PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
	public ResponseEntity<Map<String, List<Timeslot>>> getTradesForModule(@PathVariable("id") long id, Principal principal) throws Exception {
		log.info(String.format("GET REQUEST TRADEOFFERS FOR TIMESLOT WITH ID: %d BY USER: %s", id, principal.getName()));
		var out = tradeOfferService.getTradeOffersForModule(id, userService.getByUsername(principal.getName()).orElseThrow());
		return new ResponseEntity<>(out, HttpStatus.OK);
	}
}
