package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TimeTable;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.TimeTableService;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("/api/v1/timetables")
@RestController
@SecurityRequirement(name = "Authorization")

public class TimeTableController {
	
	UserService userService;
	TimeTableService timeTableService;
	
	@GetMapping
	@Operation(description = "get all timetables", operationId = "getAllTimetables")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successfully retrieved timetables"),
							@ApiResponse(responseCode = "403", description = "unauthorized fetch attempt"),
							@ApiResponse(responseCode = "400", description = "malformed request") })
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
	
/*	@GetMapping("/modulesforstudent/{studentId}")
	@Operation(description = "get potential modules for student", operationId = "getModulesForStudent")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "sucessfully fetched modules for student"),
			@ApiResponse(responseCode = "403", description = "unauthorized fetch attempt"),
			@ApiResponse(responseCode = "400", description = "malformed ID"),
			@ApiResponse(responseCode = "404", description = "unknown student id")})
	@PreAuthorize("hasRole('MEMBER')")
	public ResponseEntity<List<Timeslot>> getTimeslotsForStudent(@PathVariable("studentId") Long studentId,
																 Principal principal) throws NotFoundException {
		log.info(String.format("GET REQUEST: getModulesForStudent, by user: %s, for studentid %d",
							   principal.getName(), studentId));
		var potentialModules = moduleService.lookUpAvailableModulesForStudent(principal.getName());
		return new ResponseEntity<>(potentialModules, HttpStatus.OK);
	}*/
	
	
	@GetMapping("/suggestedTimetable/{timeslotid}")
	@Operation(description = "get suggested timetable per Module for student", operationId = "getSuggestedTimetable")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successfully fetched suggested timetable for student"),
			@ApiResponse(responseCode = "403", description = "unauthorized fetch attempt"),
			@ApiResponse(responseCode = "404", description = "unkown timeslot id"),
			@ApiResponse(responseCode = "400", description = "bad request")})
	@PreAuthorize("hasRole('MEMBER')")
	public ResponseEntity<TimeTable> getSuggestedTimetableForStudent(@PathVariable("timeslotid") Long timeslotID,
																	 Principal principal) throws NotFoundException {
		log.info(String.format("GET REQUEST: getSuggestedTimetableForStudent, by user: %s, for timeslotid: %d", principal.getName(), timeslotID));
		
		return null;
	}
	
	
}
