package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notcreated.NotCreatedException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.model.rest_models.Timetable;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.TradeOfferService;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RestController
@RequestMapping("/api/v1/users")
public class UserRestController {
	
	String BASEURL = "/api/v1/users";
	UserService userService;
	TradeOfferService tradeOfferService;
	
	@GetMapping("")
	public ResponseEntity<?> getAll(@RequestParam(value = "username", required = false) String username) throws NotFoundException {
		log.info("GET // " + BASEURL);
		if(username != null && username.length() > 0) {
			User user = userService.getByUsername(username)
					.orElseThrow(NotFoundException::new);
			return ResponseEntity.ok(user);
		}
		return ResponseEntity.ok(userService.getAll());
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<User> getById(@PathVariable Long userId) throws NotFoundException {
		log.info(String.format("GET // " + BASEURL + "/%s", userId));
		User user = userService.getById(userId)
				.orElseThrow(NotFoundException::new);
		return ResponseEntity.ok(user);
	}
	
	public ResponseEntity<User> create(@RequestBody User user, BindingResult result) {
		log.info(String.format("POST // " + BASEURL + "/%s", user.toString()));
		if(result.hasErrors()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		try {
			userService.save(user);
			log.info(String.format("SUCCESS: Created new user %s", user.getStudentNumber()));
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch(NotCreatedException e) {
			log.info(String.format("FAIL: Student %s not created", user.getStudentNumber()));
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch(IllegalArgumentException e) {
			log.info(String.format("FAIL: Student %s not created due to some error", user.getStudentNumber()));
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("/admin/{userId}")
	public ResponseEntity<User> delete(@PathVariable Long userId) throws NotFoundException {
		// TODO: Route ändern, wenn ACL fertig
		//  -> Abfrage ob Authentifiziert und Rolle berechtigt zum löschen
		log.info(String.format("DELETE // " + BASEURL + "/admin/%s", userId));
		
		User user = userService.getById(userId)
							   .orElseThrow(NotFoundException::new);
		
		userService.delete(user);
		return ResponseEntity.ok(user);
	}
	
	/**
	 * GET request handler.
	 * Provides an endpoint to {@code '/api/v1/student/<id>/personalizedTimetable'} through which an student
	 * may get his personalized timetable.
	 *
	 * @param studentId studentId to fetch timetable for
	 *
	 * @return {@link HttpStatus#OK}
	 */
	@GetMapping("/{studentId}/personalizedTimetable")
	public ResponseEntity<Timetable> getPersonalizedTimeTable(@PathVariable("studentId") long studentId) {
		log.info(String.format("Getting personalized Timetable for student: %d", studentId));
		Timetable timeTable = new Timetable();
		// TODO: anpassen an neues Model
		//var student = studentService.getById(studentId);
		log.info(String.format("Looking up possible Tradeoffers for student: %d", studentId));
		try {
			// TODO: anpassen an neues Model
//			var tradeoffers = tradeOfferService.getTradeOffersForTimeSlots(student.getTimeslots());
//			for(Timeslot timeslot : tradeoffers.keySet()) {
//				de.hsrm.mi.swtp.exchangeplatform.model.rest_models.Timeslot restTimeSlot = new de.hsrm.mi.swtp.exchangeplatform.model.rest_models.Timeslot();
//				BeanUtils.copyProperties(timeslot, restTimeSlot);
//				for(String key : tradeoffers.get(timeslot).keySet()) {
//					tradeoffers.get(timeslot).get(key).forEach(tradeOffer -> {
//						TradeOffer restOffer = new TradeOffer();
//						BeanUtils.copyProperties(tradeOffer, restOffer);
//						restTimeSlot.addPossibleTradesItem(restOffer);
//					});
//				}
//				timeTable.addTimeslotsItem(restTimeSlot);
//			}
			return new ResponseEntity<>(timeTable, HttpStatus.OK);
		} catch(RuntimeException ex) {
			log.info(String.format("Error creating dersonalized timetable for student: %d", studentId));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
}
