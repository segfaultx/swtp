package de.hsrm.mi.swtp.exchangeplatform.service;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.AppointmentNotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.NoAppointmentCapacityException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.StudentIsAlreadyAttendeeException;
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

    @Autowired(required = false)
    final JmsTemplate jmsTemplate;
    @Autowired
    final AppointmentRepository repository;

    @NonFinal
    private Integer attendeeCount = 0; // TODO: remove; is just for testing

    public List<Appointment> findAll() {
        return repository.findAll();
    }

    public Optional<Appointment> findById(Long appointmentId) {
        return repository.findById(appointmentId);
    }

    public Appointment getAppointmentById(Long appointmentId) {
        Optional<Appointment> appointmentOptional = this.findById(appointmentId);
        if (!appointmentOptional.isPresent()) throw new AppointmentNotFoundException(appointmentId);
        return appointmentOptional.get();
    }

    public void save(Appointment appointment) {
        attendeeCount++;
        repository.save(appointment);
    }

    public boolean checkCapacity(Appointment appointment) {
        return attendeeCount < appointment.getCapacity();
    }

    public void addAttendeeToAppointment(Long appointmentId, Student student) {
        Optional<Appointment> appointmentFound = this.findById(appointmentId);

        if (appointmentFound.isEmpty()) throw new AppointmentNotFoundException(appointmentId);

        Appointment appointment = appointmentFound.get();

        if (appointment.getAttendees().contains(student)) {
            throw new StudentIsAlreadyAttendeeException(student);
        }

        jmsTemplate.convertAndSend("AppointmentQueue", appointment);

        if (!this.checkCapacity(appointment) && !appointment.addAttendee(student)) {
            throw new NoAppointmentCapacityException(appointment);
        }

        log.info("========================");
        log.info("===== ADD STUDENT ======");
        log.info("========================");
    }

}
