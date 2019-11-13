package de.hsrm.mi.swtp.exchangeplatform.model;

import de.hsrm.mi.swtp.exchangeplatform.service.StudentService;
import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple accessor class which retrieves all the data from the {@link StudentService service layer}.
 */
@Builder
public class StudentAccessorImpl implements StudentAccessor {

    private final StudentService studentService;

    public StudentAccessorImpl(final StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public StudentDTO getStudent(@NotNull Long matriculationNumber) {
        return studentService.findStudent(matriculationNumber);
    }

    @Override
    public List<StudentDTO> getAllStudents() {
        return studentService.getAll();
    }

    @Override
    public List<StudentDTO> getStudents(@NotNull List<Long> matriculationNumbers) {
        return matriculationNumbers.stream()
                .map(studentService::findStudent)
                .collect(Collectors.toList());
    }
}
