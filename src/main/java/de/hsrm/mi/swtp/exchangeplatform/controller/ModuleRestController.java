package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Appointment;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import de.hsrm.mi.swtp.exchangeplatform.service.ModuleService;
import de.hsrm.mi.swtp.exchangeplatform.service.TimeTableService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * A simple rest-controller which will handle any rest calls concerning {@link Appointment Appointments}.
 * Its base url is {@code '/api/v1/appointment'}.
 * <p>
 * All fields will have
 */
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Component
@RestController
@RequestMapping("/api/v1/module")
public class ModuleRestController {

    String BASEURL = "/api/v1/module";
    ModuleService moduleService;
    TimeTableService timeTableService;

    @GetMapping
    public ResponseEntity<List<Module>> getAllModules() {
        return new ResponseEntity<>(moduleService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{timetableId}") // TODO
    public ResponseEntity<List<Module>> getAllModulesFromTimetable(@PathVariable Long timetableId) {
        return new ResponseEntity<>(moduleService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{moduleId}")
    public ResponseEntity<Module> getModuleeById(@PathVariable Long moduleId) {
        log.info(String.format("GET // " + BASEURL + "/%s", moduleId));
        try {
            return new ResponseEntity<>(
                    moduleService.getModuleById(moduleId),
                    HttpStatus.OK);
        } catch (NotFoundException e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Module> createAppointment(@RequestBody Module module, BindingResult result) {
        log.info("POST // " + BASEURL);
        log.info(module.toString());
        if (result.hasErrors()) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        moduleService.save(module);
        return new ResponseEntity<>(module, HttpStatus.OK);
    }

    @DeleteMapping("/{moduleId}")
    public ResponseEntity<Module> deleteAppointment(@PathVariable Long moduleId) {
        log.info(String.format("DELETE // " + BASEURL + "/s", moduleId));

        try {
            moduleService.delete(moduleId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}