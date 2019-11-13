package de.hsrm.mi.swtp.exchangeplatform.service;

import de.hsrm.mi.swtp.exchangeplatform.model.StudentDTO;
import de.hsrm.mi.swtp.exchangeplatform.model.StudentFactory;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Acts as a service layer on top of the persistence layer.
 * Will access the persistence lay/repository and will contain all the business logic.
 *
 * TODO: Change this for a real service class
 */
@Service
public class StudentService {

    private final int MOCK_STUDENT_COUNT = 10;
    private final HashMap<Long, StudentDTO> students;
    private final StudentFactory studentFactory;

    @Autowired
    @Builder
    public StudentService(final StudentFactory studentFactory) {
        this.students = new HashMap<>(MOCK_STUDENT_COUNT);
        this.studentFactory = studentFactory;
        this.initMockData();
    }

    @Deprecated
    private void initMockData() {
        this.studentFactory.create(MOCK_STUDENT_COUNT)
                .forEach((student) -> {
                    this.students.put(student.getMatriculationNumber(), student);
                });
    }

    /**
     * @param matriculationNumber is a student's matriculation number and unique identifier.
     * @return will return a {@link StudentDTO} object if the matriculationNumber is knwon and mapped to
     * a {@link StudentDTO} object. Otherwise will return null.
     */
    public StudentDTO findStudent(final Long matriculationNumber) {
        return this.students.getOrDefault(matriculationNumber, null);
    }

    /**
     * @return all available students.
     */
    public List<StudentDTO> getAll() {
        return new ArrayList<>(this.students.values());
    }
}
