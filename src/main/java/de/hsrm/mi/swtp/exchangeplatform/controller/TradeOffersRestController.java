package de.hsrm.mi.swtp.exchangeplatform.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager.TimeslotTopicManager;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TimeTable;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.model.rest.TradeRequest;
import de.hsrm.mi.swtp.exchangeplatform.model.rest.TradeWrapper;
import de.hsrm.mi.swtp.exchangeplatform.repository.TradeOfferRepository;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.TimeslotService;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.TradeOfferService;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.UserService;
import de.hsrm.mi.swtp.exchangeplatform.service.settings.AdminSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
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
	TimeslotService timeslotService;
	TradeOfferRepository tradeOfferRepository;
	JmsTemplate jmsTopicTemplate;
	TimeslotTopicManager timeslotTopicManager;
	ObjectMapper objectMapper;
	
	/**
	 * DELETE request handler.
	 * provides an endpoint to {@code '/api/v1/trades/<id>/<id>'} through which an {student} may delete his {@link TradeOffer}.
	 *
	 * @param studentId studentId of requester
	 * @param seekId    tradeId of tradeoffer which is to be deleted.
	 *
	 * @return {@link HttpStatus#OK} if tradeoffer was deleted, {@link HttpStatus#NOT_FOUND} if tradeoffer wasnt found,
	 * {@link HttpStatus#FORBIDDEN} if requester isnt owner of the tradeoffer.
	 */
	@DeleteMapping("/{studentId}/{seekId}")
	@Operation(description = "Delete tradeoffer with seekId of student", operationId = "deleteTradeOfferOfStudent")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successfully deleted tradeoffer"),
							@ApiResponse(responseCode = "403", description = "unauthorized delete attempt"),
							@ApiResponse(responseCode = "404", description = "tradeoffer not found") })
	@PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
	public ResponseEntity deleteTradeOffer(@Parameter(description = "Numeric ID of the student", required = true) @PathVariable("studentId") long studentId,
										   @Parameter(description = "Numeric ID of the tradeoffer", required = true) @PathVariable("seekId") long seekId
										  ) throws Exception {
		if(adminSettingsService.isTradesActive()) {
			log.info(String.format("DELETE Request Student: %d TradeOffer: %d", studentId, seekId));
			
			User offerer = userService.getById(studentId).orElseThrow(NotFoundException::new);
			
			Timeslot timeslot = timeslotService.getById(seekId).orElseThrow(NotFoundException::new);
			
			TradeOffer tradeOffer = tradeOfferRepository.findByOffererAndSeek(offerer, timeslot);
			
			if(tradeOfferService.deleteTradeOffer(tradeOffer)) {
				log.info(String.format("DELETE Request successful Student: %d TradeOffer: %d", studentId, seekId));
				return new ResponseEntity<>(HttpStatus.OK);
			}
			log.info(String.format("ERROR while DELETE Request Student: %d TradeOffer: %d - Student isn't owner of entity", studentId, seekId));
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		} else return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
		
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
	@Operation(description = "create tradeoffer for student", operationId = "createTradeOfferForStudent")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successfully created tradeoffer"),
							@ApiResponse(responseCode = "403", description = "unauthorized create attempt"),
							@ApiResponse(responseCode = "400", description = "malformed trade request") })
	@PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
	public ResponseEntity<TradeOffer> createTradeOffer(
			@Parameter(description = "Object containing ID's of student, wanted and offered timeslot", required = true) @Valid @RequestBody TradeRequest tradeRequest,
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
	@Operation(description = "request trade", operationId = "requestTrade")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successfully processed traderequest"),
							@ApiResponse(responseCode = "403", description = "unauthorized trade attempt"),
							@ApiResponse(responseCode = "400", description = "malformed trade request") })
	@PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
	public ResponseEntity<TimeTable> requestTrade(@Valid @RequestBody TradeRequest tradeRequest, Principal principal) throws Exception {
		if(adminSettingsService.isTradesActive()) {
			log.info(String.format("Traderequest of student: %d for timeslot: %d, offer: %d", tradeRequest.getOfferedByStudentMatriculationNumber(),
								   tradeRequest.getOfferedTimeslotId(), tradeRequest.getWantedTimeslotId()
								  ));
			
			User requestingUser = userService.getByUsername(principal.getName()).orElseThrow(NotFoundException::new);
			
			Timeslot offeringTimeslot = timeslotService.getById(tradeRequest.getOfferedTimeslotId()).orElseThrow(NotFoundException::new);
			
			Timeslot requestingTimeslot = timeslotService.getById(tradeRequest.getWantedTimeslotId()).orElseThrow(NotFoundException::new);

			var timeslot = tradeOfferService.tradeTimeslots(requestingUser, offeringTimeslot, requestingTimeslot, requestingUser);
			
			TimeTable timetable = new TimeTable();
			timetable.setId(tradeRequest.getOfferedByStudentMatriculationNumber());
			timetable.setDateEnd(LocalDate.now()); // DIRTY QUICK FIX
			timetable.setDateStart(LocalDate.now());// DIRTY QUICK FIX
			User user = userService.getById(tradeRequest.getOfferedByStudentMatriculationNumber()).orElseThrow(NotFoundException::new);
			
			Timeslot finalTimeslotR = requestingTimeslot;
			Topic timeslotTopic = timeslotTopicManager.getTopic(timeslot);
			
			timeslotTopicManager.getSession(timeslot)
								.createSubscriber(timeslotTopic)
								.setMessageListener(message -> {
				try {
					log.info(String.format("┌ Topic %s received message on method call: requestTrade()", timeslotTopic.toString()));
					log.info(String.format("└ Message sent to topic: %s", ((TextMessage) message).getText()));
				} catch(JMSException e) {
					log.info("└ ERROR: sent message had some kind of error.");
				}
			});
			jmsTopicTemplate.send(timeslotTopicManager.getTopic(requestingTimeslot), session -> {
				try {
					return session.createTextMessage(objectMapper.writeValueAsString(finalTimeslotR));
				} catch(JsonProcessingException e) {
					e.printStackTrace();
				}
				return session.createTextMessage("{}");
			});
			
			Timeslot finalTimeslotO = offeringTimeslot;
			
			timeslotTopicManager.getSession(timeslot)
								.createSubscriber(timeslotTopic)
								.setMessageListener(message -> {
									try {
										log.info(String.format("┌ Topic %s received message on method call: requestTrade()", timeslotTopic.toString()));
										log.info(String.format("└ Message sent to topic: %s", ((TextMessage) message).getText()));
									} catch(JMSException e) {
										log.info("└ ERROR: sent message had some kind of error.");
									}
								});
			jmsTopicTemplate.send(timeslotTopicManager.getTopic(offeringTimeslot), session -> {
				try {
					return session.createTextMessage(objectMapper.writeValueAsString(finalTimeslotO));
				} catch(JsonProcessingException e) {
					e.printStackTrace();
				}
				return session.createTextMessage("{}");
			});
			
			
			timetable.setTimeslots(user.getTimeslots());
			return new ResponseEntity<>(timetable, HttpStatus.OK);
		} else return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
	}
	
	/**
	 * POST Request handler
	 * <p>
	 * provides an endpoint to {@link User} admins to force trades
	 *
	 * @param tradeRequest object containing transaction information
	 * @param principal    admin principal
	 *
	 * @return {@link HttpStatus#OK} if success
	 *
	 * @throws Exception if failure
	 */
	
	@PostMapping("/admin")
	@Operation(description = "force admin trade", operationId = "adminForceTrade")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successfully processed traderequest"),
							@ApiResponse(responseCode = "403", description = "unauthorized trade attempt"),
							@ApiResponse(responseCode = "400", description = "malformed trade request") })
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> requestAdminTrade(@Valid @RequestBody TradeRequest tradeRequest, Principal principal) throws Exception {
		log.info(String.format("Traderequest of admin: %s for timeslot: %d, offer: %d", principal.getName(), tradeRequest.getOfferedTimeslotId(),
							   tradeRequest.getWantedTimeslotId()
							  ));
		tradeOfferService.adminTrade(tradeRequest.getOfferedByStudentMatriculationNumber(), tradeRequest.getOfferedTimeslotId(),
									 tradeRequest.getWantedTimeslotId(), userService.getByUsername(principal.getName()).orElseThrow().getId()
									);
		return new ResponseEntity<>(HttpStatus.OK);
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
	@Operation(description = "request tradeOffers", operationId = "getTradesForTimeslot")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successfully retrieved tradeoffers"),
							@ApiResponse(responseCode = "403", description = "unauthorized tradeOffer request"),
							@ApiResponse(responseCode = "400", description = "malformed tradeOffers request") })
	@PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
	public ResponseEntity<List<TradeWrapper>> getTradesForModule(@PathVariable("id") long id, Principal principal) throws Exception {
		log.info(String.format("GET REQUEST TRADEOFFERS FOR TIMESLOT WITH ID: %d BY USER: %s", id, principal.getName()));
		var out = tradeOfferService.getTradeOffersForModule(id, userService.getByUsername(principal.getName()).orElseThrow());
		return new ResponseEntity<>(out, HttpStatus.OK);
	}
	
	@GetMapping("/all")
	@Operation(description = "request all tradeOffers", operationId = "getAllTradeOffers")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successfully retrieved tradeoffers") })
	@PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
	public ResponseEntity<List<TradeOffer>> getAllTradeoffersForTest() {
		return new ResponseEntity<>(tradeOfferService.getAll(), HttpStatus.OK);
	}
	
	@GetMapping("/mytradeoffers")
	@Operation(description = "get TradeOffers for student", operationId = "getMyTradeOffers")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successfully retrieved tradeoffers"),
							@ApiResponse(responseCode = "403", description = "unauthorized request"),
							@ApiResponse(responseCode = "400", description = "malformed request") })
	@PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
	public ResponseEntity<List<TradeOffer>> getMyTradeOffers(Principal principal) throws Exception {
		log.info(String.format("GET REQUEST TRADEOFFERS FOR Student BY USER: %s", principal.getName()));
		var out = tradeOfferService.getAllTradeoffersForStudent(userService.getByUsername(principal.getName()).orElseThrow());
		return new ResponseEntity<>(out, HttpStatus.OK);
	}
}
