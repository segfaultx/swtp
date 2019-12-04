package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.NotUpdatedException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.StudentIsAlreadyAttendeeException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notcreated.NotCreatedException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.AppointmentRequestBody;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Student;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.StudentService;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.TimeslotService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * A simple rest-controller which will handle any rest calls concerning {@link Timeslot Appointments}.
 * Its base url is {@code '/api/v1/appointment'}.
 * <p>
 */
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Component
@RestController
@RequestMapping("/api/v1/appointment")
public class AppointmentRestController {
	
	String BASEURL = "/api/v1/appointment";
	TimeslotService timeslotService;
	StudentService studentService;
	
	/**
	 * GET request handler.
	 * Will handle any request GET request on {@code '/api/v1/appointment'}.
	 *
	 * @return a JSON made up of a list containing all available {@link Timeslot Appointments}.
	 * If there are none will return an empty list.
	 */
	@GetMapping
	public ResponseEntity<List<Timeslot>> getAllAppointments() {
		return new ResponseEntity<>(timeslotService.getAll(), HttpStatus.OK);
	}
	
	/**
	 * GET request handler.
	 * Will handle any request GET request to {@code '/api/v1/appointment/<id>'}.
	 *
	 * @param appointmentId is the id of an {@link Timeslot}.
	 *
	 * @return {@link HttpStatus#OK} and the requested {@link Timeslot} instance if it is found. Otherwise will return {@link HttpStatus#BAD_REQUEST}
	 */
	@GetMapping("/{appointmentId}")
	public ResponseEntity<Timeslot> getTimeTableById(@PathVariable Long appointmentId) {
		log.info(String.format("GET // " + BASEURL + "/%s", appointmentId));
		try {
			return new ResponseEntity<>(timeslotService.getById(appointmentId), HttpStatus.OK);
		} catch(NotFoundException e) {
			log.info(e.getMessage());
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	/**
	 * POST request handler.
	 * Provides an endpoint to {@code '/api/v1/appointment'} through which an {admin} may create a new {@link Timeslot} for a module.
	 *
	 * @param timeslot a new appointment which is to be created and inserted into the database.
	 *
	 * @return {@link HttpStatus#OK} and the created appointment if was created successfully. Otherwise will return {@link HttpStatus#BAD_REQUEST}.
	 */
	@PostMapping
	public ResponseEntity<Timeslot> createAppointment(@RequestBody Timeslot timeslot, BindingResult result) {
		log.info("POST // " + BASEURL);
		log.info(timeslot.toString());
		if(result.hasErrors()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		try {
			timeslotService.save(timeslot);
		} catch(NotCreatedException e) {
			log.info(String.format("FAIL: Appointment %s not created", timeslot.getId()));
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch(IllegalArgumentException e) {
			log.info(String.format("FAIL: Appointment %s not created due to some error", timeslot.getId()));
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(timeslot, HttpStatus.OK);
	}
	
	/**
	 * POST request handler.
	 * Provides an endpoint to {@code '/api/v1/appointment/join'} through which a user ({@link Student}) may join an {@link Timeslot}.
	 *
	 * @param appointmentRequestBody is an object which contains the id of an {@link Timeslot} and the matriculation number of a {@link Student}.
	 *
	 * @return {@link HttpStatus#OK} and the updated appointment if the user joined successfully. Otherwise will return {@link HttpStatus#BAD_REQUEST}.
	 */
	@PostMapping("/join")
	public ResponseEntity<Timeslot> joinAppointment(@RequestBody AppointmentRequestBody appointmentRequestBody, BindingResult result) {
		log.info("POST // " + BASEURL + "/join");
		log.info(appointmentRequestBody.toString());
		if(result.hasErrors()) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		
		try {
			timeslotService.addAttendeeToAppointment(appointmentRequestBody.getAppointmentId(),
													 this.studentService.getById(appointmentRequestBody.getMatriculationNumber())
													);
			return new ResponseEntity<>(timeslotService.getById(appointmentRequestBody.getMatriculationNumber()), HttpStatus.OK);
		} catch(StudentIsAlreadyAttendeeException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch(NotFoundException e) {
			log.info(String.format("FAIL: Student %s not found", appointmentRequestBody.getMatriculationNumber()));
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * POST request handler.
	 * Provides an endpoint to {@code '/api/v1/appointment/leave'} through which a user ({@link Student}) can lean an {@link Timeslot}.
	 *
	 * @param appointmentRequestBody is an object which contains the id of an {@link Timeslot} and the matriculation number of a {@link Student}.
	 *
	 * @return {@link HttpStatus#OK} and the updated appointment if the user left successfully. Otherwise will return {@link HttpStatus#BAD_REQUEST}.
	 */
	@PostMapping("/leave")
	public ResponseEntity<Timeslot> leaveAppointment(@RequestBody AppointmentRequestBody appointmentRequestBody, BindingResult result) {
		log.info("POST // " + BASEURL + "/leave");
		log.info(appointmentRequestBody.toString());
		if(result.hasErrors()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		try {
			timeslotService.removeAttendeeFromAppointment(appointmentRequestBody.getAppointmentId(),
														  this.studentService.getById(appointmentRequestBody.getMatriculationNumber())
														 );
			return new ResponseEntity<>(timeslotService.getById(appointmentRequestBody.getMatriculationNumber()), HttpStatus.OK);
		} catch(NotFoundException e) {
			log.info(String.format("FAIL: Student %s not found", appointmentRequestBody.getMatriculationNumber()));
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * PATCH request handler.
	 * Provides an endpoint to {@code '/api/v1/appointment/<id>'} through which an {@link Timeslot} can be updated.
	 *
	 * @param appointmentId is the id of the appointment which is to be updated/modified.
	 * @param timeslot      is an object which contains the date of an the updated {@link Timeslot}.
	 *
	 * @return {@link HttpStatus#OK} and the updated appointment if the appointment was updated successfully. Otherwise will return {@link HttpStatus#BAD_REQUEST}.
	 */
	@PatchMapping("/{appointmentId}")
	public ResponseEntity<Student> updateAppointment(@PathVariable Long appointmentId, @RequestBody Timeslot timeslot, BindingResult result) {
		log.info(String.format("PATCH // " + BASEURL + "/%s", appointmentId));
		if(result.hasErrors()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		try {
			timeslotService.update(appointmentId, timeslot);
			log.info(String.format("SUCCESS: Updated appointment %s", appointmentId));
			return new ResponseEntity<>(studentService.getById(appointmentId), HttpStatus.OK);
		} catch(NotUpdatedException e) {
			log.info(String.format("FAIL: Something went wrong during update of appointment %s", appointmentId));
			return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
		}
	}
	
	/**
	 * DELETE request handler.
	 * Provides an endpoint to {@code '/api/v1/appointment/admin/<id>'} through which an {admin} may delete {@link Timeslot}.
	 *
	 * @param appointmentId the id of an appointment which is to be deleted.
	 *
	 * @return {@link HttpStatus#OK} if the appointment was removed successfully. Otherwise will return {@link HttpStatus#BAD_REQUEST}.
	 */
	@DeleteMapping("/admin/{appointmentId}")
	public ResponseEntity<Timeslot> deleteAppointment(@PathVariable Long appointmentId) {
		log.info(String.format("DELETE // " + BASEURL + "/admin/s", appointmentId));
		
		try {
			timeslotService.delete(appointmentId);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch(IllegalArgumentException e) {
			log.info(String.format("FAIL: Appointment %s not deleted due to error while parsing", appointmentId));
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
}
