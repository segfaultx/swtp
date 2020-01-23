package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.UserIsAlreadyAttendeeException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.ModuleRequestBody;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.ModuleService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

import java.security.Principal;
import java.util.List;

/**
 * A simple rest-controller which will handle any rest calls concerning {@link Module Modules}.
 * Its base url is {@code '/api/v1/module'}.
 * <p>
 */
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@SecurityRequirement(name = "Authorization")
@RequestMapping("/api/v1/modules")
@RestController
public class ModuleRestController {
	
	String BASEURL = "/api/v1/modules";
	ModuleService moduleService;
	UserService userService;
	TimeTableService timeTableService;
	
	@GetMapping("")
	public ResponseEntity<List<Module>> getAll() {
		return new ResponseEntity<>(moduleService.getAll(), HttpStatus.OK);
	}
	
	@GetMapping("/{moduleId}")
	@Operation(description = "get module by id", operationId= "getModuleById")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successfully retrieved module"),
							@ApiResponse(responseCode = "403", description = "unauthorized fetch attempt"),
							@ApiResponse(responseCode = "400", description = "malformed ID") })
	@PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
	public ResponseEntity<Module> getById(@PathVariable Long moduleId) throws NotFoundException {
		log.info(String.format("GET // " + BASEURL + "/%s", moduleId));
		
		Module module = moduleService.getById(moduleId)
				.orElseThrow(NotFoundException::new);
		return new ResponseEntity<>(module, HttpStatus.OK);

	}
	
	/**
	 * POST request handler.
	 * Provides an endpoint to {@code '/api/v1/module/join'} through which a user ({@link User}) may join a {@link Module}.
	 *
	 * @param moduleRequestBody is an object which contains the id of an {@link Module} and the student ID of a {@link User}.
	 *
	 * @return {@link HttpStatus#OK} and the updated module if the user joined successfully. Otherwise will return {@link HttpStatus#BAD_REQUEST}.
	 */
	@PostMapping("/join")
	@Operation(description = "join module", operationId = "joinModule")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successfully joined appointment"),
							@ApiResponse(responseCode = "403", description = "unauthorized join attempt"),
							@ApiResponse(responseCode = "400", description = "malformed request") })
	@PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
	public ResponseEntity<Module> joinAppointment(@RequestBody ModuleRequestBody moduleRequestBody, BindingResult result) throws NotFoundException {
		log.info("POST // " + BASEURL + "/join");
		log.info(moduleRequestBody.toString());
		if(result.hasErrors()) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		
		User user = userService.getById(moduleRequestBody.getStudentId())
							   .orElseThrow(NotFoundException::new);
		
		try {
			moduleService.addAttendeeToModule(moduleRequestBody.getModuleId(), user);
			Module module = moduleService.getById(moduleRequestBody.getModuleId())
											   .orElseThrow(NotFoundException::new);
			return ResponseEntity.ok(module);
		} catch(UserIsAlreadyAttendeeException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * POST request handler.
	 * Provides an endpoint to {@code '/api/v1/modules/leave'} through which a user ({@link User}) can leave a {@link Module}.
	 *
	 * @param moduleRequestBody is an object which contains the id of a {@link Module} and the student ID of an {@link User}.
	 *
	 * @return {@link HttpStatus#OK} and the updated Module if the user left successfully. Otherwise will return {@link HttpStatus#BAD_REQUEST}.
	 */
	@PostMapping("/leave")
	@Operation(description = "leave module", operationId = "leaveModule")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successfully left module"),
							@ApiResponse(responseCode = "403", description = "unauthorized leave attempt"),
							@ApiResponse(responseCode = "400", description = "malformed leave request") })
	@PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
	public ResponseEntity<HttpStatus> leaveModule(@RequestBody ModuleRequestBody moduleRequestBody, BindingResult result) throws NotFoundException {
		log.info("POST // " + BASEURL + "/leave");
		log.info(moduleRequestBody.toString());
		if(result.hasErrors()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		User user = userService.getById(moduleRequestBody.getStudentId())
							   .orElseThrow(NotFoundException::new);
		
		moduleService.removeStudentFromModule(moduleRequestBody.getModuleId(), user);
		Module module = moduleService.getById(moduleRequestBody.getModuleId())
										   .orElseThrow(NotFoundException::new);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/modulesforstudent/{studentId}")
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
	}
}