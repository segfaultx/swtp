package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TimeTable;
import de.hsrm.mi.swtp.exchangeplatform.repository.TimeTableRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
