package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TimeTable;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.TimeTableService;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("/api/v1/timetables")
@RestController
public class TimeTableController {
	
	UserService userService;
	TimeTableService timeTableService;
	
	@GetMapping
	@ApiOperation(value = "get all timetables", nickname = "getAllTimetables")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "successfully retrieved timetables"),
							@ApiResponse(code = 403, message = "unauthorized fetch attempt"),
							@ApiResponse(code = 400, message = "malformed request") })
	@PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
	public ResponseEntity<List<TimeTable>> getAll() {
		return new ResponseEntity<>(timeTableService.getAll(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
	public ResponseEntity<TimeTable> getById(@PathVariable Long id) throws Exception {
		
		TimeTable timeTable = timeTableService.getById(id);
		
		return ResponseEntity.ok(timeTable);
	}
}
