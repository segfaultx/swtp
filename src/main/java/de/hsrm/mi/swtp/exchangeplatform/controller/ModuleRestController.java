package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.ModuleService;
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

import java.util.List;

/**
 * A simple rest-controller which will handle any rest calls concerning {@link Module Modules}.
 * Its base url is {@code '/api/v1/module'}.
 * <p>
 */
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("/api/v1/modules")
@RestController
public class ModuleRestController {
	
	String BASEURL = "/api/v1/modules";
	ModuleService moduleService;
	
	public ResponseEntity<List<Module>> getAll() {
		return new ResponseEntity<>(moduleService.getAll(), HttpStatus.OK);
	}
	
	@GetMapping("/{moduleId}")
	@ApiOperation(value = "get module by id", nickname = "getModuleById")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "successfully retrieved module"),
							@ApiResponse(code = 403, message = "unauthorized fetch attempt"),
							@ApiResponse(code = 400, message = "malformed ID") })
	@PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
	public ResponseEntity<Module> getById(@PathVariable Long moduleId) throws NotFoundException {
		log.info(String.format("GET // " + BASEURL + "/%s", moduleId));
		
		Module module = moduleService.getById(moduleId)
				.orElseThrow(NotFoundException::new);
		return new ResponseEntity<>(module, HttpStatus.OK);

	}
}