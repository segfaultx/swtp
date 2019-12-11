package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TimeTable;
import de.hsrm.mi.swtp.exchangeplatform.model.rest_models.TimetableDTO;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.RestConverterService;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.StudentService;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.TimeTableService;
import io.swagger.annotations.ApiOperation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("/api/v1/timetables")
@RestController
public class TimeTableController implements BaseRestController<TimeTable, Long> {
	
	TimeTableService timeTableService;
	StudentService studentService;
	RestConverterService restConverterService;
	
	@Override
	@GetMapping
	public ResponseEntity<List<TimeTable>> getAll() {
		return new ResponseEntity<>(timeTableService.getAll(), HttpStatus.OK);
	}
	
	@Override
	@GetMapping("/{id}")
	public ResponseEntity<TimeTable> getById(@PathVariable Long id) {
		try {
			return new ResponseEntity<>(timeTableService.getById(id), HttpStatus.OK);
		} catch(NotFoundException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@Override
	@PostMapping
	public ResponseEntity<TimeTable> create(@RequestBody TimeTable timeTable, BindingResult result) {
		if(result.hasErrors()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		timeTableService.save(timeTable);
		return new ResponseEntity<>(timeTable, HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<TimeTable> update(@PathVariable Long id, @RequestBody TimeTable timeTable, BindingResult result) {
		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
	}
	
	@Override
	public ResponseEntity<TimeTable> delete(@PathVariable Long id) {
		try {
			this.timeTableService.delete(id);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch(NotFoundException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch(IllegalArgumentException e) {
			log.info(String.format("FAIL: Something went wrong during deletion of timetable %s", id));
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@ApiOperation(value = "Get personalized Timetable of student.", nickname = "getTimetableForStudent", notes = "", response = TimetableDTO.class, tags = { })
	@RequestMapping(value = "/{studentId}", produces = { "application/json" }, method = RequestMethod.GET)
	public ResponseEntity<TimetableDTO> getTimetableForStudent(@PathVariable(value = "studentId") Long studentId) {
		var stud = studentService.getById(studentId);
		TimeTable timeTable = new TimeTable();
		timeTable.setTimeslots(stud.getTimeslots());
		TimetableDTO out = (TimetableDTO) restConverterService.convert(timeTable);
		out.setId(stud.getStudentId());
		return new ResponseEntity<>(out, HttpStatus.OK);
	}
}
