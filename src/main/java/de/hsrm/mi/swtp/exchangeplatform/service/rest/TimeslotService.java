package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.UserIsAlreadyAttendeeException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notcreated.TimeslotNotCreatedException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.repository.TimeslotRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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

	public List<Timeslot> getAll() {
		return repository.findAll();
	}
	
	public void save(Timeslot timeslot) {
		if(this.repository.existsById(timeslot.getId())) {
			log.info(String.format("FAIL: Timeslot %s not created", timeslot));
			throw new TimeslotNotCreatedException(timeslot);
		}
		repository.save(timeslot);
		log.info(String.format("SUCCESS: Timeslot %s created", timeslot));
	}
	
	public Optional<Timeslot> getById(Long timeslotId) {
		return repository.findById(timeslotId);
	}
	
	public void addAttendeeToTimeslot(Long timeslotId, User student) throws NotFoundException {
		Timeslot timeslot = getById(timeslotId)
				.orElseThrow(NotFoundException::new);
		
		if(timeslot.getAttendees().contains(student)) {
			log.info(String.format("FAIL: Student %s is already an attendee", student.getStudentNumber()));
			throw new UserIsAlreadyAttendeeException(student);
		}
		
		save(timeslot);
		log.info(String.format("SUCCESS: Student %s added to appointment %s", student.getStudentNumber(), timeslotId));
	}


	public void removeAttendeeFromTimeslot(Long timeslotId, User student) throws NotFoundException {
		Timeslot timeslot = getById(timeslotId)
								.orElseThrow(NotFoundException::new);
		
		List<User> attendees = timeslot.getAttendees();
		attendees.remove(student);
		timeslot.setAttendees(attendees);
		
		save(timeslot);
	}
	
	public boolean isCapacityLeft(Timeslot timeslot) {
		return timeslot.getAttendees().size() < timeslot.getCapacity();
	}

	public int getAttendeeCount(Timeslot timeslot) {
		return timeslot.getAttendees().size();
	}
}
