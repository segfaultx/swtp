package de.hsrm.mi.swtp.exchangeplatform.service;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.AppointmentNotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.StudentNotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Appointment;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Student;
import de.hsrm.mi.swtp.exchangeplatform.repository.AppointmentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppointmentService {
    
    @Autowired
    final JmsTemplate jmsTemplate;
    @Autowired
    final AppointmentRepository repository;
    
    @NonFinal
    private Integer attendeeCount = 0; // TODO: remove; is just for testing
    
    public List<Appointment> findAll() {
        return repository.findAll();
    }
    
    public Optional<Appointment> findById(Long id) {
        return repository.findById(id);
    }
    
    public void save(Appointment appointment) {
        repository.save(appointment);
    }
    
    public boolean checkCapacity(Appointment appointment) {
        return attendeeCount < appointment.getCapacity();
    }
    
    public void addAttendeeToAppointment(Long appointmentId, Optional<Student> studentOptional) {
        Optional<Appointment> appointmentFound = this.findById(appointmentId);
        
        if (appointmentFound.isEmpty()) throw new AppointmentNotFoundException();
        if (studentOptional.isEmpty()) throw new StudentNotFoundException();
        
        Appointment appointment = appointmentFound.get();
        jmsTemplate.convertAndSend("AppointmentQueue", appointment);
        
        if (!this.checkCapacity(appointment) && !appointment.addAttendee(studentOptional.get())) {
            throw new NotFoundException();
        }
        attendeeCount++;
        log.info("========================");
        log.info("===== ADD STUDENT ======");
        log.info("========================");
    }
    
}
