package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.NotUpdatedException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Student;
import de.hsrm.mi.swtp.exchangeplatform.service.StudentService;
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
@RequestMapping("/api/v1/student")
public class StudentController {

    String BASEURL = "/api/v1/student";
    StudentService studentService;

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        log.info("GET // " + BASEURL);
        return new ResponseEntity<>(studentService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{matriculationNumber}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long matriculationNumber) {
        log.info(String.format("GET // " + BASEURL + "/%s", matriculationNumber));
        try {
            return new ResponseEntity<>(studentService.getStudentById(matriculationNumber), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student, BindingResult result) {
        log.info(String.format("POST // " + BASEURL + "/%s", student.toString()));
        if (result.hasErrors()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        studentService.save(student);
        log.info(String.format("Created new student: id=%s", student.getMatriculationNumber()));
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @PatchMapping("/{matriculationNumber}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long matriculationNumber, @RequestBody Student student, BindingResult result) {
        log.info(String.format("GET // " + BASEURL + "/%s", matriculationNumber));
        if (result.hasErrors()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        try {
            studentService.updateStudentById(matriculationNumber, student);
            log.info(String.format("Updated student: id=%s", matriculationNumber));
            return new ResponseEntity<>(studentService.getStudentById(matriculationNumber), HttpStatus.OK);
        } catch (NotUpdatedException e) {
            log.info(String.format("Something went wrong during update of student: id=%s", matriculationNumber));
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }

    }
}
