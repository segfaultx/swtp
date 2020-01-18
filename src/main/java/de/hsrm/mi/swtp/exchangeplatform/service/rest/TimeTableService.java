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
	
	public TimeTable getSuggestedTimetable(Long timeslotID, String username){
		TimeTable tempTimetable = new TimeTable();
		var user = userRepository.findByUsername(username).orElseThrow();
		var timeslot = timeslotRepository.findById(timeslotID).orElseThrow();
		var module = timeslot.getModule();
		var allTimeslotsOfModule = module.getTimeslots();
		tempTimetable.setTimeslots(user.getTimeslots());
		List<Timeslot> potentialTimeslots = new ArrayList<>();
		allTimeslotsOfModule.forEach(ts -> { if (hasTimeslotSpace(ts)) potentialTimeslots.add(ts);});
		if(potentialTimeslots.size() < 2) return null;
		if(potentialTimeslots.stream().noneMatch(item -> item.getTimeSlotType() == TypeOfTimeslots.VORLESUNG)) return null;
		//TODO: überprüfen welche Timeslots collision haben, wenn alle -> Micha fragen wie man damit umgeht?, ansonsten ersten ohne Collision in timetable einbauen
		return null;
	}
	
	public boolean hasCollisions(Timeslot timeslot, TimeTable timeTable){
		for(Timeslot ts : timeTable.getTimeslots()) {
			if (ts.getDay() == timeslot.getDay()){
				if(hoursAreColliding(ts.getTimeEnd(), ts.getTimeStart(), timeslot.getTimeEnd(), timeslot.getTimeStart())) return true;
			}
		}
		return false;
	}
	
	public boolean hoursAreColliding(LocalTime aTimeEnd, LocalTime aTimeStart, LocalTime bTimeEnd, LocalTime bTimeStart){
		if(aTimeStart.equals(bTimeStart)  || aTimeEnd.equals(bTimeEnd)) return true;
		if((aTimeStart.isBefore(bTimeEnd) && aTimeStart.isAfter(bTimeStart)) || (bTimeStart.isBefore(aTimeEnd) && bTimeStart.isAfter(aTimeStart))) return true;
		if((aTimeStart.isBefore(bTimeStart) && aTimeEnd.isBefore(bTimeEnd)) || (bTimeStart.isBefore(aTimeStart) && bTimeEnd.isBefore(aTimeEnd))) return true;
		return false;
	}
	
	public boolean hasTimeslotSpace(Timeslot timeslot){
		return (timeslot.getCapacity() - timeslot.getAttendees().size()) > 0;
	}
}
