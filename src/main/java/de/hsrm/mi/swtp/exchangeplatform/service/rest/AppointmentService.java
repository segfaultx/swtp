package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.NoAppointmentCapacityException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.NotUpdatedException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.StudentIsAlreadyAttendeeException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notcreated.AppointmentNotCreatedException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.ModelNotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
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
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppointmentService implements RestService<Appointment, Long> {

    @Autowired
    final JmsTemplate jmsTemplate;
    @Autowired
    final AppointmentRepository repository;

    @NonFinal
    private Integer attendeeCount = 0; // TODO: remove; is just for testing

    @Override
    public List<Appointment> getAll() {
        return repository.findAll();
    }

    @Override
    public Appointment getById(Long appointmentId) {
        Optional<Appointment> appointmentOptional = this.repository.findById(appointmentId);
        if (!appointmentOptional.isPresent()) throw new NotFoundException(appointmentId);
        return appointmentOptional.get();
    }

    @Override
    public void save(Appointment appointment) {
        if (this.repository.existsById(appointment.getId())) {
            log.info(String.format("FAIL: Appointment %s not created", appointment));
            throw new AppointmentNotCreatedException(appointment);
        }
        repository.save(appointment);
        log.info(String.format("SUCCESS: Appointment %s created", appointment));
    }

    @Override
    public void delete(Long id) throws IllegalArgumentException {
        this.repository.delete(this.getById(id));
        log.info(String.format("SUCCESS: Appointment %s deleted", id));
    }

    @Override
    public boolean update(Long appointmentId, Appointment update) throws IllegalArgumentException {
        Appointment appointment = this.getById(appointmentId);

        if (!Objects.equals(appointment.getId(), update.getId())) throw new NotUpdatedException();

        log.info("Updating appointment..");
        log.info(appointment.toString() + " -> " + update.toString());
        appointment.setCapacity(update.getCapacity());
        appointment.setDay(update.getDay());
        appointment.setLecturer(update.getLecturer());
        appointment.setModule(update.getModule());
        appointment.setRoom(update.getRoom());
        appointment.setTimeStart(update.getTimeStart());
        appointment.setTimeEnd(update.getTimeEnd());
        appointment.setType(update.getType());

        this.save(appointment);

        return true;
    }

    public boolean checkCapacity(Appointment appointment) {
        return attendeeCount < appointment.getCapacity();
    }

    public void addAttendeeToAppointment(Long appointmentId, Student student) {
        Appointment appointment = this.getById(appointmentId);

        if (appointment.getAttendees().contains(student)) {
            log.info(String.format("FAIL: Student %s is already an attendee",
                    student.getMatriculationNumber()));
            throw new StudentIsAlreadyAttendeeException(student);
        }

        jmsTemplate.convertAndSend("AppointmentQueue", appointment);

        if (!this.checkCapacity(appointment) && !appointment.addAttendee(student)) {
            log.info(String.format(
                    "FAIL: Student %s not added to appointment %s",
                    student.getMatriculationNumber(),
                    appointmentId));
            throw new NoAppointmentCapacityException(appointment);
        }
        attendeeCount++;
        log.info(String.format(
                "SUCCESS: Student %s added to appointment %s",
                student.getMatriculationNumber(),
                appointmentId));
    }

    public void removeAttendeeFromAppointment(Long appointmentId, Student student) {
        Appointment appointment = this.getById(appointmentId);

        jmsTemplate.convertAndSend("AppointmentQueue", appointment);

        if (!appointment.removeAttendee(student)) {
            log.info(String.format(
                    "FAIL: Student %s not removed", student.getMatriculationNumber()));
            throw new ModelNotFoundException(student);
        }
        attendeeCount--;
        log.info(String.format(
                "SUCCESS: Student %s removed from appointment %s",
                student.getMatriculationNumber(),
                appointmentId));
    }

}
