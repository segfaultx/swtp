package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.model.data.TimeTable;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.StudentService;
import io.swagger.annotations.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("/api/v1/timetables")
@RestController
@Api(value = "timetables", description = "the timetables API")
public class TimeTableController {
	
	StudentService studentService;
	
	/**
	 * GET request handler
	 * <p>
	 * provides an endpoint to {@code '/api/v1/timetables'} for users to retrieve their timetables
	 *
	 * @param studentId id of student to fetch timetable for
	 *
	 * @return timetable of student
	 */
	@ApiOperation(value = "Get personalized Timetable of student.", nickname = "getTimetableForStudent", notes = "", response = TimeTable.class, tags = { })
	@GetMapping(value = "/{studentId}", produces = { "application/json" })
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = "a timetable", response = TimeTable.class) })
	public ResponseEntity<TimeTable> getTimetableForStudent(
			@ApiParam(value = "Numeric ID of the student", required = true) @PathVariable(value = "studentId") Long studentId
														   ) {
		var stud = studentService.getById(studentId);
		TimeTable timeTable = new TimeTable();
		timeTable.setTimeslots(stud.getTimeslots());
		timeTable.setId(stud.getStudentId());
		timeTable.setDateEnd(LocalDate.now());
		timeTable.setDateStart(LocalDate.now()); // todo: fix this shit
		return new ResponseEntity<>(timeTable, HttpStatus.OK);
	}
}
