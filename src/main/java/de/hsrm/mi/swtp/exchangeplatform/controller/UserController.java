package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.NotUpdatedException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notcreated.NotCreatedException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Student;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.StudentService;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.UserService;
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

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Component
@RestController
@RequestMapping("/api/v1/user")

/**
 * Same Controller as StudentController and may deleted in future because it depends on matriculationnumbers and not on unique usernames.
 */

public class UserController {
    String BASEURL = "/api/v1/student";
    UserService UserService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("GET // " + BASEURL);
        return new ResponseEntity<>(UserService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{matriculationNumber}")
    public ResponseEntity<User> getUserById(@PathVariable Long matriculationNumber) {
        log.info(String.format("GET // " + BASEURL + "/%s", matriculationNumber));
        try {
            return new ResponseEntity<>(UserService.getById(matriculationNumber), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
