package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.model.StudentAccessorImpl;
import de.hsrm.mi.swtp.exchangeplatform.model.StudentDTO;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * This Rest-Controller will provide a REST-Api handling all request concerning student data.
 */
@RequestMapping("/students")
@Builder
public class StudentRestController {

    private final StudentAccessorImpl studentAccessor;

    public StudentRestController(final StudentAccessorImpl studentAccessor) {
        this.studentAccessor = studentAccessor;
    }

    @GetMapping("/")
    public ResponseEntity<StudentDTO> get() {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{matriculationNumber}")
    public ResponseEntity<StudentDTO> getStudentByMatricsNumber(@PathVariable @NotNull final Long matriculationNumber) {
        return new ResponseEntity<>(
                studentAccessor
                        .getStudent(matriculationNumber),
                HttpStatus.ACCEPTED
        );
    }

    @GetMapping("/all")
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        return new ResponseEntity<>(
                studentAccessor.getAllStudents(),
                HttpStatus.ACCEPTED
        );
    }

}
