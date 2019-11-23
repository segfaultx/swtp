package de.hsrm.mi.swtp.exchangeplatform.service;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.StudentNotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.StudentNotUpdatedException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Student;
import de.hsrm.mi.swtp.exchangeplatform.repository.StudentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StudentService {

    @Autowired
    StudentRepository repository;

    public List<Student> findAll() {
        return repository.findAll();
    }

    public Optional<Student> findById(Long matriculationNumber) {
        return repository.findById(matriculationNumber);
    }

    public Student getStudentById(Long matriculationNumber) {
        Optional<Student> studentOptional = this.findById(matriculationNumber);
        if (!studentOptional.isPresent()) throw new StudentNotFoundException(matriculationNumber);
        return studentOptional.get();
    }

    public void save(Student student) {
        repository.save(student);
    }

    public boolean updateStudentById(Long matriculationNumber, Student update) {
        Student student = this.getStudentById(matriculationNumber);

        if (!Objects.equals(student.getMatriculationNumber(), update.getMatriculationNumber())) {
            throw new StudentNotUpdatedException();
        }

        log.info("Updating student.");
        log.info(update.toString());
        log.info(student.toString());

        student.setTimeslots(update.getTimeslots());
        student.setUsername(update.getUsername());
        student.setFirstName(update.getFirstName());
        student.setLastName(update.getLastName());

        this.save(student);

        return true;
    }

}
