package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TimeTable;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.TypeOfTimeslots;
import de.hsrm.mi.swtp.exchangeplatform.repository.ModuleRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.TimeTableRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.TimeslotRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TimeTableService {
	
	TimeTableRepository repository;
	UserRepository userRepository;
	TimeslotRepository timeslotRepository;
	
	public List<TimeTable> getAll() {
		return repository.findAll();
	}
	
	public TimeTable getById(Long id) throws Exception {
		var user = userRepository.findById(id).orElseThrow();
		if (user.getStudentNumber() == null) throw new NotFoundException();
		TimeTable out = new TimeTable();
		out.setDateStart(LocalDate.now());
		out.setDateEnd(LocalDate.now());
		out.setId(user.getStudentNumber());
		out.setTimeslots(user.getTimeslots());
		return out;
	}
	
	public void save(TimeTable timeTable) {
		repository.save(timeTable);
	}
	
	/**
	 *
	 * @param timeslotID
	 * @param username
	 * @return
	 */
	public TimeTable getSuggestedTimetable(Long timeslotID, String username){
		TimeTable tempTimetable = new TimeTable();
		var user = userRepository.findByUsername(username).orElseThrow();
		var timeslot = timeslotRepository.findById(timeslotID).orElseThrow();
		var module = timeslot.getModule();
		var allTimeslotsOfModule = module.getTimeslots();
		tempTimetable.setTimeslots(user.getTimeslots());
		List<Timeslot> potentialTimeslots = new ArrayList<>();
		allTimeslotsOfModule.forEach(ts -> { if (hasTimeslotSpace(ts)) potentialTimeslots.add(ts);}); //Wird geaendert sobald PO Restriktion steht
		if(potentialTimeslots.size() < 2) return null;
		if(potentialTimeslots.stream().noneMatch(item -> item.getTimeSlotType() == TypeOfTimeslots.VORLESUNG)) return null;
		//TODO: Mit Micha klaeren, was passiert wenn VL mit Timetable kollidiert
		//Vorlesung der temporaeren Timetable hinzufuegen und aus den potentialtimeslots loeschen
		for(Timeslot ts: potentialTimeslots) {
			if(ts.getTimeSlotType() == TypeOfTimeslots.VORLESUNG){
				tempTimetable.getTimeslots().add(ts);
				potentialTimeslots.remove(ts);
				break;
			}
		}
		//Ersten Timeslot der keine Kollision hat der temporaeren Timetable hinzufuegen
		for(Timeslot ts: potentialTimeslots) {
			if(!hasCollisions(ts, tempTimetable)){
				tempTimetable.getTimeslots().add(ts);
				break;
			}
		}
		return tempTimetable;
	}
	
	
	/**
	 *
	 * @param timeslot
	 * @param timeTable
	 * @return
	 */
	public boolean hasCollisions(Timeslot timeslot, TimeTable timeTable){
		for(Timeslot ts : timeTable.getTimeslots()) {
			if (ts.getDay() == timeslot.getDay()){
				if(hoursAreColliding(ts.getTimeEnd(), ts.getTimeStart(), timeslot.getTimeEnd(), timeslot.getTimeStart())) return true;
			}
		}
		return false;
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
