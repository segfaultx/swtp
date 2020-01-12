package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.UserIsAlreadyAttendeeException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.TimeslotRequestBody;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.TimeslotService;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.UserService;
import io.swagger.annotations.ApiOperation;
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

import java.util.List;

/**
 * A simple rest-controller which will handle any rest calls concerning {@link Timeslot Appointments}.
 * Its base url is {@code '/api/v1/timeslot'}.
 * <p>
 */
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@SecurityRequirement(name = "Authorization")
@RestController
@RequestMapping("/api/v1/timeslots")
public class TimeslotRestController {

	String BASEURL = "/api/v1/timeslots";
	TimeslotService timeslotService;
	UserService userService;

	/**
	 * GET request handler.
	 * Will handle any request GET request on {@code '/api/v1/timeslot'}.
	 *
	 * @return a JSON made up of a list containing all available {@link Timeslot Appointments}.
	 * If there are none will return an empty list.
	 */
	@GetMapping("")
	public ResponseEntity<List<Timeslot>> getAll() {
		return new ResponseEntity<>(timeslotService.getAll(), HttpStatus.OK);
	}

	/**
	 * GET request handler.
	 * Will handle any request GET request to {@code '/api/v1/timeslot/<id>'}.
	 *
	 * @param id is the id of an {@link Timeslot}.
	 *
	 * @return {@link HttpStatus#OK} and the requested {@link Timeslot} instance if it is found. Otherwise will return {@link HttpStatus#BAD_REQUEST}
	 */
	@GetMapping("/{id}")
	@ApiOperation(value = "get user by id", nickname = "getUserById")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "successfully retrieved user"),
							@ApiResponse(code = 403, message = "unauthorized fetch attempt"),
							@ApiResponse(code = 400, message = "malformed userID") })
	@PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
	public ResponseEntity<Timeslot> getById(@PathVariable Long id) throws NotFoundException {
		log.info(String.format("GET // " + BASEURL + "/%s", id));
		Timeslot timeslot = timeslotService.getById(id)
				.orElseThrow(NotFoundException::new);
		return ResponseEntity.ok(timeslot);
	}

	/**
	 * POST request handler.
	 * Provides an endpoint to {@code '/api/v1/timeslot/join'} through which a user ({@link User}) may join an {@link Timeslot}.
	 *
	 * @param timeslotRequestBody is an object which contains the id of an {@link Timeslot} and the student ID of a {@link User}.
	 *
	 * @return {@link HttpStatus#OK} and the updated timeslot if the user joined successfully. Otherwise will return {@link HttpStatus#BAD_REQUEST}.
	 */
	@PostMapping("/join")
	@ApiOperation(value = "join timeslot", nickname = "joinAppointment")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "successfully joined appointment"),
							@ApiResponse(code = 403, message = "unauthorized join attempt"),
							@ApiResponse(code = 400, message = "malformed request") })
	@PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
	public ResponseEntity<Timeslot> joinAppointment(@RequestBody TimeslotRequestBody timeslotRequestBody, BindingResult result) throws NotFoundException {
		log.info("POST // " + BASEURL + "/join");
		log.info(timeslotRequestBody.toString());
		if(result.hasErrors()) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		
		User user = userService.getById(timeslotRequestBody.getStudentId())
							   .orElseThrow(NotFoundException::new);

		try {
			timeslotService.addAttendeeToTimeslot(timeslotRequestBody.getTimeslotId(), user);
			Timeslot timeslot = timeslotService.getById(timeslotRequestBody.getStudentId())
											   .orElseThrow(NotFoundException::new);
			return ResponseEntity.ok(timeslot);
		} catch(UserIsAlreadyAttendeeException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * POST request handler.
	 * Provides an endpoint to {@code '/api/v1/timeslot/leave'} through which a user ({@link User}) can lean an {@link Timeslot}.
	 *
	 * @param timeslotRequestBody is an object which contains the id of an {@link Timeslot} and the student ID of a {@link User}.
	 *
	 * @return {@link HttpStatus#OK} and the updated timeslot if the user left successfully. Otherwise will return {@link HttpStatus#BAD_REQUEST}.
	 */
	@PostMapping("/leave")
	@ApiOperation(value = "leave timeslot", nickname = "leaveTimeslot")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "successfully left timeslot"),
							@ApiResponse(code = 403, message = "unauthorized leave attempt"),
							@ApiResponse(code = 400, message = "malformed leave request") })
	@PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
	public ResponseEntity<Timeslot> leaveAppointment(@RequestBody TimeslotRequestBody timeslotRequestBody, BindingResult result) throws NotFoundException {
		log.info("POST // " + BASEURL + "/leave");
		log.info(timeslotRequestBody.toString());
		if(result.hasErrors()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		User user = userService.getById(timeslotRequestBody.getStudentId())
							   .orElseThrow(NotFoundException::new);
		
		timeslotService.removeAttendeeFromTimeslot(timeslotRequestBody.getTimeslotId(), user);
		
		Timeslot timeslot = timeslotService.getById(timeslotRequestBody.getStudentId())
				.orElseThrow(NotFoundException::new);
		return ResponseEntity.ok(timeslot);
	}

}