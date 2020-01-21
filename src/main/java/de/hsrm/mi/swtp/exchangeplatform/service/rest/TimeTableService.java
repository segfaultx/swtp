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
}
