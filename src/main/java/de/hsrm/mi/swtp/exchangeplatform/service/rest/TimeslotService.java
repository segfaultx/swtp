package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notcreated.TimeslotNotCreatedException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.repository.TimeslotRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TimeslotService {

	TimeslotRepository repository;

	@NonFinal
	private Integer attendeeCount = 0; // TODO: remove; is just for testing

	public List<Timeslot> getAll() {
		return repository.findAll();
	}

	public void addAttendeeToTimeslot(Long timeslotId, User student) throws NotFoundException {
		Timeslot timeslot = getById(timeslotId)
				.orElseThrow(NotFoundException::new);

		// TODO: Anpassen nach Model Umstellung
//		if(timeslot.getAttendees().contains(student)) {
//			log.info(String.format("FAIL: Student %s is already an attendee", student.getStudentId()));
//			throw new StudentIsAlreadyAttendeeException(student);
//		}
//
//		if(!this.checkCapacity(timeslot) && !timeslot.addAttendee(student)) {
//			log.info(String.format("FAIL: Student %s not added to appointment %s", student.getStudentId(), timeslotId));
//			throw new NoTimeslotCapacityException(timeslot);
//		}
		attendeeCount++;
		this.save(timeslot);
		log.info(String.format("SUCCESS: Student %s added to appointment %s", student.getStudentNumber(), timeslotId));
	}

	public Optional<Timeslot> getById(Long timeslotId) {
		return repository.findById(timeslotId);
	}

	public void save(Timeslot timeslot) {
		if(this.repository.existsById(timeslot.getId())) {
			log.info(String.format("FAIL: Appointment %s not created", timeslot));
			throw new TimeslotNotCreatedException(timeslot);
		}
		repository.save(timeslot);
		log.info(String.format("SUCCESS: Appointment %s created", timeslot));
	}

	public void removeAttendeeFromTimeslot(Long timeslotId, User student) throws NotFoundException {
//		Timeslot timeslot = this.getById(timeslotId)
//				.orElseThrow(NotFoundException::new);
//
//		// TODO: Anpassen nach Model umstellung
//		if(!timeslot.removeAttendee(student)) {
//			log.info(String.format("FAIL: Student %s not removed", student.getStudentId()));
//			throw new ModelNotFoundException(student);
//		}
//		attendeeCount--;
//		messageSender.send(timeslot);
//		log.info(String.format("SUCCESS: Student %s removed from appointment %s", student.getStudentNumber(), timeslotId));
	}


	public boolean checkCapacity(Timeslot timeslot) {
		return attendeeCount < timeslot.getCapacity();
	}


}
