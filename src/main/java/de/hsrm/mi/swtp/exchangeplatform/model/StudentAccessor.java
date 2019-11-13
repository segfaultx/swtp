package de.hsrm.mi.swtp.exchangeplatform.model;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface StudentAccessor {

    StudentDTO getStudent(@NotNull final Long matriculationNumber);

    List<StudentDTO> getAllStudents();

    List<StudentDTO> getStudents(@NotNull final List<Long> matriculationNumbers);

}
