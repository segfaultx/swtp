package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Appointment;
import de.hsrm.mi.swtp.exchangeplatform.model.data.AppointmentRequestBody;
import de.hsrm.mi.swtp.exchangeplatform.service.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/appointment")
public class AppointmentRestController {

    private final AppointmentService appointmentService;

    public AppointmentRestController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return new ResponseEntity<>(appointmentService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getTimeTableById(@PathVariable Long id) {
        Appointment found = appointmentService.findById(id)
                .orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(found, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Appointment> postStudentAppointment(@RequestBody AppointmentRequestBody appointmentRequestBody, BindingResult result) {
        if (result.hasErrors()) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        Optional<Appointment> appointmentFound = appointmentService.findById(appointmentRequestBody.getAppointmentId());
        if (appointmentFound.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Appointment appointment = appointmentFound.get();

        if (appointmentService.checkCapacity(appointment)) {
            Appointment appointmentUpd = appointmentService
                    .findById(appointment.getId())
                    .orElseThrow(NotFoundException::new);
            appointmentUpd.getAttendees().add(appointmentRequestBody.getStudent());
            appointmentService.save(appointmentUpd);
            return new ResponseEntity<>(appointmentUpd, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}