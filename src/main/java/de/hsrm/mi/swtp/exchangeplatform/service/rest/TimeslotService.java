package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.NoTimeslotCapacityException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.UserIsAlreadyAttendeeException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.ModelNotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.LeaveTimeslotSuccessfulMessage;
import de.hsrm.mi.swtp.exchangeplatform.messaging.sender.PersonalMessageSender;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.TypeOfTimeslots;
import de.hsrm.mi.swtp.exchangeplatform.repository.TimeslotRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Timeslot service class for manipulationg timeslot data
 */
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TimeslotService {

	TimeslotRepository repository;
	PersonalMessageSender personalMessageSender;
	
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
		// check if student is on waitlist or already an attendee
		if(timeslot.getWaitList().contains(student) || timeslot.getAttendees().contains(student)) {
			log.info(String.format("FAIL: Student %s is already an attendee or on the waitlist", student.getStudentNumber()));
			throw new UserIsAlreadyAttendeeException(student);
		}
		
		timeslot.getWaitList().add(student);
		this.save(timeslot);
		log.info(String.format("SUCCESS: Student %s added to waitlist %s", student.getStudentNumber(), timeslotId));
	}
	
	public Timeslot addAttendeeToTimeslot(Timeslot timeslot, User student) throws UserIsAlreadyAttendeeException, NoTimeslotCapacityException {
		// check if alreadx an attendee
		if(timeslot.getAttendees().contains(student)) {
			log.info(String.format("FAIL: Student %s is already an attendee", student.getStudentNumber()));
			throw new UserIsAlreadyAttendeeException(student);
		}
		// check if capacity has been reached
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
		// check waitlist and add first in line to timeslot
		if(!timeslot.getWaitList().isEmpty()){
			User nextStudent = timeslot.getWaitList().get(0);
			timeslot.getWaitList().remove(nextStudent);
			timeslot.getAttendees().add(nextStudent);
		}
		
		personalMessageSender.send(student, LeaveTimeslotSuccessfulMessage.builder()
																	   .timeslot(timeslot)
																	   .time(LocalTime.now())
																	   .build());
		
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
	
	// check set capacity against seats filled
	public boolean checkCapacity(Timeslot timeslot) {
		return timeslot.getAttendees().size() < timeslot.getCapacity();
	}
	
	
	/**
	 * Searches for the first timeslot of the seeked module with no collisions
	 * @param timeslotID The seeked module
	 * @param user The requestor
	 * @return List of suggested Timeslots
	 */
	public List<Timeslot> getSuggestedTimeslots(Long timeslotID, User user){
		List<Timeslot> suggestedTimeslots = new ArrayList<>();
		//var user = userRepository.findById(studentID).orElseThrow();
		var timeslot = repository.findById(timeslotID).orElseThrow();
		var module = timeslot.getModule();
		var potentialTimeslots = module.getTimeslots();
		
		//Vorlesung der temporaeren Timetable hinzufuegen und aus den potentialtimeslots loeschen
		for(Timeslot ts: potentialTimeslots) {
			if(ts.getTimeSlotType() == TypeOfTimeslots.VORLESUNG){
				suggestedTimeslots.add(ts);
				potentialTimeslots.remove(ts);
				break;
			}
		}
		//Ersten Timeslot der keine Kollision hat der suggestedTimeslots Liste hinzufuegen
		for(Timeslot ts: potentialTimeslots) {
			if(!hasCollisions(ts, user.getTimeslots())){
				suggestedTimeslots.add(ts);
				break;
			}
		}
		return suggestedTimeslots;
	}
	
	
	/**
	 * Method to check if a given Timeslot and a Timetable has collisions
	 * @param timeslot The potential Timeslot for User
	 * @param timeTable List of Timeslots from User
	 * @return true if successful
	 */
	public boolean hasCollisions(Timeslot timeslot, List<Timeslot> timeTable){
		for(Timeslot ts : timeTable) {
			if (ts.getDay() == timeslot.getDay()){
				if(hoursAreColliding(ts.getTimeEnd(), ts.getTimeStart(), timeslot.getTimeEnd(), timeslot.getTimeStart())) return true;
			}
		}
		return false;
	}
	/**
	 * Method to check if startTime and endTime of 2 Timeslots are colliding
	 * @param aTimeEnd EndTime of Timeslot A
	 * @param aTimeStart StartTime of Timeslot A
	 * @param bTimeEnd EndTime of Timeslot B
	 * @param bTimeStart StartTime of Timeslot B
	 * @return true if successful
	 */
	public boolean hoursAreColliding(LocalTime aTimeEnd, LocalTime aTimeStart, LocalTime bTimeEnd, LocalTime bTimeStart){
		if(aTimeStart.equals(bTimeStart)  || aTimeEnd.equals(bTimeEnd)) return true;
		if((aTimeStart.isBefore(bTimeEnd) && aTimeStart.isAfter(bTimeStart)) || (bTimeStart.isBefore(aTimeEnd) && bTimeStart.isAfter(aTimeStart))) return true;
		return (aTimeStart.isBefore(bTimeStart) && aTimeEnd.isBefore(bTimeEnd)) || (bTimeStart.isBefore(aTimeStart) && bTimeEnd.isBefore(aTimeEnd));
	}
}
