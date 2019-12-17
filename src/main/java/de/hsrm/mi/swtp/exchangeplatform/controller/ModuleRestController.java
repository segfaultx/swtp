package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notcreated.NotCreatedException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.ModuleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
	
	String BASEURL = "/api/v1/module";
	ModuleService moduleService;
	
	public ResponseEntity<List<Module>> getAll() {
		return new ResponseEntity<>(moduleService.getAll(), HttpStatus.OK);
	}
	
	@GetMapping("/{moduleId}")
	public ResponseEntity<Module> getById(@PathVariable Long moduleId) throws NotFoundException {
		log.info(String.format("GET // " + BASEURL + "/%s", moduleId));
		
		Module module = moduleService.getById(moduleId)
				.orElseThrow(NotFoundException::new);
		return new ResponseEntity<>(module, HttpStatus.OK);

	}
}