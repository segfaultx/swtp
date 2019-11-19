package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.messaging.AppointmentMessageConverter;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Appointment;
import de.hsrm.mi.swtp.exchangeplatform.model.data.AppointmentRequestBody;
import de.hsrm.mi.swtp.exchangeplatform.service.AppointmentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.List;
import java.util.Optional;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Component
@Setter
@RestController
@RequestMapping("/api/v1/appointment")
public class AppointmentRestController {

    @Autowired
    AppointmentService appointmentService;
    @Autowired
    JmsTemplate jmsTemplate;

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
        jmsTemplate.convertAndSend("AppointmentQueue", appointment);

        try {
            appointmentService.addAttendeeToAppointment(appointmentRequestBody.getAppointmentId(), appointmentRequestBody.getStudent());
            return new ResponseEntity<>(appointment, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}