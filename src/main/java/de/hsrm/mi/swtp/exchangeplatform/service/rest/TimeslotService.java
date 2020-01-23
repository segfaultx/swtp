package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.NoTimeslotCapacityException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.UserIsAlreadyAttendeeException;
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
import java.time.LocalTime;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TimeslotService {

	TimeslotRepository repository;
	
	public List<Timeslot> getAll() {
		return repository.findAll();
	}

	public Optional<Timeslot> getById(Long timeslotId) {
		return repository.findById(timeslotId);
	}

	public Timeslot save(Timeslot timeslot) {
		log.info(String.format("SUCCESS: Timeslot %s created", timeslot));
		return repository.save(timeslot);
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
	
	public Timeslot addAttendeeToTimeslot(Timeslot timeslot, User student) throws UserIsAlreadyAttendeeException, NoTimeslotCapacityException {
		
		if(timeslot.getAttendees().contains(student)) {
			log.info(String.format("FAIL: Student %s is already an attendee", student.getStudentNumber()));
			throw new UserIsAlreadyAttendeeException(student);
		}
		
		if(!this.checkCapacity(timeslot)){
			log.info(String.format("FAIL: Student %s not added to appointment %s", student.getStudentNumber(), timeslot.getId()));
			throw new NoTimeslotCapacityException(timeslot);
		}
		
		timeslot.addAttendee(student);
		
		log.info(String.format("SUCCESS: Student %s added to appointment %s", student.getStudentNumber(), timeslot.getId()));
		return save(timeslot);
	}

	public Timeslot removeAttendeeFromTimeslot(Timeslot timeslot, User student) throws NotFoundException {
		
		if(!timeslot.getAttendees().contains(student)) {
			log.info(String.format("FAIL: Student %s not removed", student.getStudentNumber()));
			throw new ModelNotFoundException(student);
		}
		
		timeslot.removeAttendee(student);
		
		if(!timeslot.getWaitList().isEmpty()){
			User nextStudent = timeslot.getWaitList().get(0);
			timeslot.getWaitList().remove(nextStudent);
			timeslot.getAttendees().add(nextStudent);
		}
		
		log.info(String.format("SUCCESS: Student %s removed from appointment %s", student.getStudentNumber(), timeslot.getId()));
		return save(timeslot);
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
	
	/**
	 * Method to check if startTime and endTime of 2 Timeslots are colliding
	 * @param aTimeEnd
	 * @param aTimeStart
	 * @param bTimeEnd
	 * @param bTimeStart
	 * @return true if successful
	 */
	public boolean hoursAreColliding(LocalTime aTimeEnd, LocalTime aTimeStart, LocalTime bTimeEnd, LocalTime bTimeStart){
		if(aTimeStart.equals(bTimeStart)  || aTimeEnd.equals(bTimeEnd)) return true;
		if((aTimeStart.isBefore(bTimeEnd) && aTimeStart.isAfter(bTimeStart)) || (bTimeStart.isBefore(aTimeEnd) && bTimeStart.isAfter(aTimeStart))) return true;
		if((aTimeStart.isBefore(bTimeStart) && aTimeEnd.isBefore(bTimeEnd)) || (bTimeStart.isBefore(aTimeStart) && bTimeEnd.isBefore(aTimeEnd))) return true;
		return false;
	}
	
	/**
	 * Helper method to check if timeslot has space
	 * @param timeslot
	 * @return true if successful
	 */
	public boolean hasTimeslotSpace(Timeslot timeslot){
		return (timeslot.getCapacity() - timeslot.getAttendees().size()) > 0;
	}
}
