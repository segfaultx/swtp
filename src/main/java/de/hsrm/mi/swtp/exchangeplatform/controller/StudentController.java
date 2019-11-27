package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.NotUpdatedException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notcreated.NotCreatedException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Student;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TimeTable;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.StudentService;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.TradeOfferService;
import lombok.AccessLevel;
import lombok.Getter;
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
    TradeOfferService tradeOfferService;

    /**
     * GET request handler.
     * Will handle any request GET request on {@code '/api/v1/student'}.
     *
     * @return a JSON made up of a list containing all available {@link Student Students}. If there are none will return an empty list.
     */
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        log.info("GET // " + BASEURL);
        return new ResponseEntity<>(studentService.getAll(), HttpStatus.OK);
    }

    /**
     * GET request handler.
     * Will handle any request GET request to {@code '/api/v1/student/<id>'}.
     *
     * @param matriculationNumber is the id of a {@link Student}.
     * @return {@link HttpStatus#OK} and the requested {@link Student} instance if it is found. Otherwise will return {@link HttpStatus#BAD_REQUEST}
     */
    @GetMapping("/{matriculationNumber}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long matriculationNumber) {
        log.info(String.format("GET // " + BASEURL + "/%s", matriculationNumber));
        try {
            return new ResponseEntity<>(studentService.getById(matriculationNumber), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * POST request handler.
     * Provides an endpoint to {@code '/api/v1/student'} through which an {admin} may create a new {@link Student}.
     *
     * @param student a new student which is to be created and inserted into the database.
     * @return {@link HttpStatus#OK} and the created student if was created successfully. Otherwise will return {@link HttpStatus#BAD_REQUEST}.
     */
    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student,
                                                 BindingResult result) {
        log.info(String.format("POST // " + BASEURL + "/%s", student.toString()));
        if (result.hasErrors()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        try {
            studentService.save(student);
            log.info(String.format("SUCCESS: Created new student %s", student.getMatriculationNumber()));
            return new ResponseEntity<>(student, HttpStatus.OK);
        } catch (NotCreatedException e) {
            log.info(String.format("FAIL: Student %s not created", student.getMatriculationNumber()));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            log.info(String.format("FAIL: Student %s not created due to some error", student.getMatriculationNumber()));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * PATCH request handler.
     * Provides an endpoint to {@code '/api/v1/student/<id>'} through which an {@link Student} can be updated.
     *
     * @param matriculationNumber is the id of the student which is to be updated/modified.
     * @param student             is an object which contains the date of an the updated {@link Student}.
     * @return {@link HttpStatus#OK} and the updated student if the student was updated successfully. Otherwise will return {@link HttpStatus#BAD_REQUEST}.
     */
    @PatchMapping("/{matriculationNumber}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long matriculationNumber,
                                                 @RequestBody Student student,
                                                 BindingResult result) {
        log.info(String.format("PATCH // " + BASEURL + "/%s", matriculationNumber));
        if (result.hasErrors()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        try {
            studentService.update(matriculationNumber, student);
            log.info(String.format("SUCCESS: Updated student: id=%s", matriculationNumber));
            return new ResponseEntity<>(studentService.getById(matriculationNumber), HttpStatus.OK);
        } catch (NotFoundException e) {
            log.info(String.format("FAIL: Student %s not found", matriculationNumber));
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (NotUpdatedException e) {
            log.info(String.format("FAIL: Something went wrong during update of student: id=%s", matriculationNumber));
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }

    }

    /**
     * DELETE request handler.
     * Provides an endpoint to {@code '/api/v1/student/admin/<id>'} through which an {admin} may delete {@link Student}.
     *
     * @param matriculationNumber the id of an student which is to be deleted.
     * @return {@link HttpStatus#OK} if the student was removed successfully. Otherwise will return {@link HttpStatus#BAD_REQUEST}.
     */
    @DeleteMapping("/admin/{matriculationNumber}")
    public ResponseEntity<Student> deleteStudent(@PathVariable Long matriculationNumber) {
        log.info(String.format("DELETE // " + BASEURL + "/admin/%s", matriculationNumber));

        try {
            this.studentService.delete(matriculationNumber);
            log.info(String.format("SUCCESS: Deleted student %s", matriculationNumber));
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        } catch (NotFoundException e) {
            log.info(String.format("FAIL: Student %s not found", matriculationNumber));
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            log.info(String.format("FAIL: Something went wrong during deletion of student %s", matriculationNumber));
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    /**
     * GET request handler.
     * Provides an endpoint to {@code '/api/v1/student/<id>/personalizedTimetable'} through which an student
     * may get his personalized timetable.
     *
     * @param studentid studentId to fetch timetable for
     * @return {@link HttpStatus#OK}
     */
    @GetMapping("/{studentId}/personalizedTimetable")
    public TimeTable getPersonalizedTimeTable(@PathVariable("studentId") long studentid){
        TimeTable timeTable = new TimeTable();
        return  null;
    }
}
