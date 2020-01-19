package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.NoTimeslotCapacityException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.UserIsAlreadyAttendeeException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notcreated.TimeslotNotCreatedException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.ModelNotFoundException;
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

	public void addAttendeeToTimeslot(Long timeslotId, User student) throws NotFoundException {
		Timeslot timeslot = getById(timeslotId)
				.orElseThrow(NotFoundException::new);
		
			if(timeslot.getAttendees().contains(student)) {
				log.info(String.format("FAIL: Student %s is already an attendee", student.getStudentNumber()));
				throw new UserIsAlreadyAttendeeException(student);
			}
	
			if(!this.checkCapacity(timeslot)){
				log.info(String.format("FAIL: Student %s not added to appointment %s", student.getStudentNumber(), timeslotId));
				throw new NoTimeslotCapacityException(timeslot);
			}
		
		timeslot.getAttendees().add(student);
		this.save(timeslot);
		log.info(String.format("SUCCESS: Student %s added to appointment %s", student.getStudentNumber(), timeslotId));
	}

	public Optional<Timeslot> getById(Long timeslotId) {
		return repository.findById(timeslotId);
	}

	public void save(Timeslot timeslot) {
		/*if(this.repository.existsById(timeslot.getId())) {
			log.info(String.format("FAIL: Appointment %s not created", timeslot));
			throw new TimeslotNotCreatedException(timeslot);
		} */
		repository.save(timeslot);
		log.info(String.format("SUCCESS: Appointment %s created", timeslot));
	}
	
	public void addAttendeeToWaitlist(Long timeslotId, User student) throws NotFoundException {
		Timeslot timeslot = getById(timeslotId)
				.orElseThrow(NotFoundException::new);
		
		if(timeslot.getWaitList().contains(student) || timeslot.getAttendees().contains(student)) {
			log.info(String.format("FAIL: Student %s is already an attendee or on the waitlist", student.getStudentNumber()));
			throw new UserIsAlreadyAttendeeException(student);
		}
		
		timeslot.getWaitList().add(student);
		this.save(timeslot);
		log.info(String.format("SUCCESS: Student %s added to waitlist %s", student.getStudentNumber(), timeslotId));
	}

	public void removeAttendeeFromTimeslot(Long timeslotId, User student) throws NotFoundException {
		Timeslot timeslot = this.getById(timeslotId)
				.orElseThrow(NotFoundException::new);
		
		if(!timeslot.getAttendees().contains(student)) {
			log.info(String.format("FAIL: Student %s not removed", student.getStudentNumber()));
			throw new ModelNotFoundException(student);
		}
		timeslot.getAttendees().remove(student);
		if(!timeslot.getWaitList().isEmpty()){
			User nextStudent = timeslot.getWaitList().get(0);
			timeslot.getWaitList().remove(nextStudent);
			timeslot.getAttendees().add(nextStudent);
		}
		this.save(timeslot);
		log.info(String.format("SUCCESS: Student %s removed from appointment %s", student.getStudentNumber(), timeslotId));
	}
	
	public void removeAttendeeFromWaitlist(Long timeslotId, User student) throws NotFoundException {
		Timeslot timeslot = this.getById(timeslotId)
								.orElseThrow(NotFoundException::new);
		
		if(!timeslot.getWaitList().contains(student)) {
			log.info(String.format("FAIL: Student %s not removed", student.getStudentNumber()));
			throw new ModelNotFoundException(student);
		}
		
		timeslot.getWaitList().remove(student);
		this.save(timeslot);
		log.info(String.format("SUCCESS: Student %s removed from appointment %s", student.getStudentNumber(), timeslotId));
	}
	
	public boolean checkCapacity(Timeslot timeslot) {
		return timeslot.getAttendees().size() < timeslot.getCapacity();
	}
}
