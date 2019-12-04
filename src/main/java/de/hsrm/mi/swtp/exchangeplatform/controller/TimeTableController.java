package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TimeTable;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.TimeTableService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Component
@RestController
@RequestMapping("/api/v1/timetable")
public class TimeTableController {
	
	TimeTableService timeTableService;
	
	@GetMapping
	public ResponseEntity<List<TimeTable>> getAllTimeTables() {
		return new ResponseEntity<>(timeTableService.getAll(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<TimeTable> getTimeTableById(@PathVariable Long id) {
		try {
			return new ResponseEntity<>(timeTableService.getById(id), HttpStatus.OK);
		} catch(NotFoundException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping
	public ResponseEntity<TimeTable> createTimeTable(@RequestBody TimeTable timeTable, BindingResult result) {
		if(result.hasErrors()) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		
		timeTableService.save(timeTable);
		return new ResponseEntity<>(timeTable, HttpStatus.OK);
	}
}
