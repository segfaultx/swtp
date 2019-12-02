package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.NoTimeslotCapacityException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.NotUpdatedException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.StudentIsAlreadyAttendeeException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notcreated.TimeslotNotCreatedException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.ModelNotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.messaging.sender.TimeslotMessageSender;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Student;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.repository.TimeslotRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TimeslotService implements RestService<Timeslot, Long> {

    TimeslotRepository repository;
    TimeslotMessageSender messageSender;

    @NonFinal
    private Integer attendeeCount = 0; // TODO: remove; is just for testing

    @Override
    public List<Timeslot> getAll() {
        return repository.findAll();
    }

    @Override
    public Timeslot getById(Long timeslotId) {
        Optional<Timeslot> timeslotOptional = this.repository.findById(timeslotId);
        if (!timeslotOptional.isPresent()) throw new NotFoundException(timeslotId);
        return timeslotOptional.get();
    }

    @Override
    public void save(Timeslot timeslot) {
        if (this.repository.existsById(timeslot.getId())) {
            log.info(String.format("FAIL: Appointment %s not created", timeslot));
            throw new TimeslotNotCreatedException(timeslot);
        }
        repository.save(timeslot);
        log.info(String.format("SUCCESS: Appointment %s created", timeslot));
        messageSender.send(timeslot);
    }

    @Override
    public void delete(Long id) throws IllegalArgumentException {
        this.repository.delete(this.getById(id));
        log.info(String.format("SUCCESS: Appointment %s deleted", id));
    }

    @Override
    public boolean update(Long timeslotId, Timeslot update) throws IllegalArgumentException {
        Timeslot timeslot = this.getById(timeslotId);

        if (!Objects.equals(timeslot.getId(), update.getId())) throw new NotUpdatedException();

        log.info("Updating appointment..");
        log.info(timeslot.toString() + " -> " + update.toString());
        timeslot.setCapacity(update.getCapacity());
        timeslot.setDay(update.getDay());
        timeslot.setLecturer(update.getLecturer());
        timeslot.setModule(update.getModule());
        timeslot.setRoom(update.getRoom());
        timeslot.setTimeStart(update.getTimeStart());
        timeslot.setTimeEnd(update.getTimeEnd());
        timeslot.setType(update.getType());

        this.save(timeslot);

        return true;
    }

    public boolean checkCapacity(Timeslot timeslot) {
        return attendeeCount < timeslot.getCapacity();
    }

    public void addAttendeeToTimeslot(Long timeslotId, Student student) {
        Timeslot timeslot = this.getById(timeslotId);

        if (timeslot.getAttendees().contains(student)) {
            log.info(String.format("FAIL: Student %s is already an attendee",
                    student.getMatriculationNumber()));
            throw new StudentIsAlreadyAttendeeException(student);
        }

        if (!this.checkCapacity(timeslot) && !timeslot.addAttendee(student)) {
            log.info(String.format(
                    "FAIL: Student %s not added to appointment %s",
                    student.getMatriculationNumber(),
                    timeslotId));
            throw new NoTimeslotCapacityException(timeslot);
        }
        attendeeCount++;
        this.save(timeslot);
        log.info(String.format(
                "SUCCESS: Student %s added to appointment %s",
                student.getMatriculationNumber(),
                timeslotId));
    }

    public void removeAttendeeFromTimeslot(Long timeslotId, Student student) {
        Timeslot timeslot = this.getById(timeslotId);

        if (!timeslot.removeAttendee(student)) {
            log.info(String.format(
                    "FAIL: Student %s not removed", student.getMatriculationNumber()));
            throw new ModelNotFoundException(student);
        }
        attendeeCount--;
        messageSender.send(timeslot);
        log.info(String.format(
                "SUCCESS: Student %s removed from appointment %s",
                student.getMatriculationNumber(),
                timeslotId));
    }

}
