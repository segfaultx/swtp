package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.StudentIsAlreadyAttendeeException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Appointment;
import de.hsrm.mi.swtp.exchangeplatform.model.data.AppointmentRequestBody;
import de.hsrm.mi.swtp.exchangeplatform.service.AppointmentService;
import de.hsrm.mi.swtp.exchangeplatform.service.StudentService;
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
 * A simple rest-controller which will handle any rest calls concerning {@link Appointment Appointments}.
 * Its base url is {@code '/api/v1/appointment'}.
 * <p>
 * All fields will have
 */
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Component
@RestController
@RequestMapping("/api/v1/appointment")
public class AppointmentRestController {

    String BASEURL = "/api/v1/appointment";
    AppointmentService appointmentService;
    StudentService studentService;

    /**
     * GET request handler.
     * Will handle any request GET request on {@code '/api/v1/appointment'}.
     *
     * @return a JSON made up of a list containing all available {@link Appointment Appointments}.
     * If there are none will return an empty list.
     */
    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return new ResponseEntity<>(appointmentService.findAll(), HttpStatus.OK);
    }

    /**
     * GET request handler.
     * Will handle any request GET request to {@code '/api/v1/appointment/<id>'}.
     *
     * @param appointmentId is the id of an {@link Appointment}.
     * @return the requested {@link Appointment} instance if it is found. Otherwise will
     */
    @GetMapping("/{appointmentId}")
    public ResponseEntity<Appointment> getTimeTableById(@PathVariable Long appointmentId) {
        log.info(String.format("GET // " + BASEURL + "/%s", appointmentId));
        try {
            return new ResponseEntity<>(
                    appointmentService.getAppointmentById(appointmentId),
                    HttpStatus.OK);
        } catch (NotFoundException e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/join")
    public ResponseEntity<Appointment> joinAppointment(@RequestBody AppointmentRequestBody appointmentRequestBody, BindingResult result) {
        log.info("POST // " + BASEURL + "/join");
        log.info(appointmentRequestBody.toString());
        if (result.hasErrors()) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        try {
            appointmentService.addAttendeeToAppointment(
                    appointmentRequestBody.getAppointmentId(),
                    this.studentService.getStudentById(appointmentRequestBody.getMatrimatriculationNumber())
            );
            log.info("Student %s join appointment %s",
                    appointmentRequestBody.getMatrimatriculationNumber(),
                    appointmentRequestBody.getAppointmentId());
            return new ResponseEntity<>(
                    appointmentService.getAppointmentById(appointmentRequestBody.getMatrimatriculationNumber()),
                    HttpStatus.OK
            );
        } catch (StudentIsAlreadyAttendeeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@RequestBody Appointment appointment, BindingResult result) {
        log.info("POST // " + BASEURL);
        log.info(appointment.toString());
        if (result.hasErrors()) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        appointmentService.save(appointment);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }
}