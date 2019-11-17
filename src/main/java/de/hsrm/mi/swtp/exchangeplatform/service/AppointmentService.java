package de.hsrm.mi.swtp.exchangeplatform.service;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Appointment;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Student;
import de.hsrm.mi.swtp.exchangeplatform.repository.AppointmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AppointmentService {

    private final AppointmentRepository repository;
    private Integer attendeeCount = 0;

    public AppointmentService(AppointmentRepository repository) {
        log.info("==== ==== ==== ==== ==== ====");
        log.info("==== APPOINTMENT SERVICE ====");
        log.info("==== ==== ==== ==== ==== ====");
        this.repository = repository;
    }

    public List<Appointment> findAll() {
        return repository.findAll();
    }

    public Optional<Appointment> findById(Long id) {
        return repository.findById(id);
    }

    public void save(Appointment appointment) {
        attendeeCount++;
        repository.save(appointment);
    }

    public boolean checkCapacity(Appointment appointment) {
        return attendeeCount < appointment.getCapacity();
    }

    public void addAttendeeToAppointment(Long appointmentId, Student student) {
        this.findById(appointmentId)
                .ifPresentOrElse(appointment -> {
                            if (!this.checkCapacity(appointment) && !appointment.addAttendee(student)) {
                                throw new NotFoundException();
                            }
//                            this.save(appointment);
                            log.info("========================");
                            log.info("===== ADD STUDENT ======");
                            log.info("========================");
                        },
                        NotFoundException::new);
    }

}
