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
@RequestMapping("/api/v1/module")
@RestController
public class ModuleRestController implements BaseRestController<Module, Long> {

    String BASEURL = "/api/v1/module";
    ModuleService moduleService;

    @Override
    public ResponseEntity<List<Module>> getAll() {
        return new ResponseEntity<>(moduleService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}/timeslots")
    public ResponseEntity<List<Timeslot>> getAllTimeslotsOfModule(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(moduleService.getById(id).getTimeslots(), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @GetMapping("/{moduleId}")
    public ResponseEntity<Module> getById(@PathVariable Long moduleId) {
        log.info(String.format("GET // " + BASEURL + "/%s", moduleId));
        try {
            return new ResponseEntity<>(
                    moduleService.getById(moduleId),
                    HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<Module> create(@RequestBody Module module,
                                         BindingResult result) {
        log.info("POST // " + BASEURL);
        log.info(module.toString());
        if (result.hasErrors()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        try {
            moduleService.save(module);
            return new ResponseEntity<>(module, HttpStatus.OK);
        } catch (NotCreatedException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @PatchMapping("/{moduleId}")
    public ResponseEntity<Module> update(@PathVariable Long moduleId,
                                         @RequestBody Module model,
                                         BindingResult result) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    @DeleteMapping("/{moduleId}")
    public ResponseEntity<Module> delete(@PathVariable Long moduleId) {
        log.info(String.format("DELETE // " + BASEURL + "/admin/s", moduleId));

        try {
            moduleService.delete(moduleId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            log.info(String.format("FAIL: Module %s not deleted due to error while parsing", moduleId));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}