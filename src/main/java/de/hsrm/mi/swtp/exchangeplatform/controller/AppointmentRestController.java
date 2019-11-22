package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Appointment;
import de.hsrm.mi.swtp.exchangeplatform.model.data.AppointmentRequestBody;
import de.hsrm.mi.swtp.exchangeplatform.service.AppointmentService;
import de.hsrm.mi.swtp.exchangeplatform.service.StudentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    StudentService studentService;
    
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
    
    @PostMapping("/join")
    public ResponseEntity<Appointment> joinAnAppointment(@RequestBody AppointmentRequestBody appointmentRequestBody, BindingResult result) {
        if (result.hasErrors()) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        
        try {
            appointmentService.addAttendeeToAppointment(
                    appointmentRequestBody.getAppointmentId(),
                    this.studentService.findById(appointmentRequestBody.getMatrimatriculationNumber())
            );
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping
    public ResponseEntity<Appointment> createNewAppointment(@RequestBody Appointment appointment, BindingResult result) {
        if (result.hasErrors()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        
        try {
            appointmentService.save(appointment);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}