package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.StudentNotUpdatedException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notcreated.NotCreatedException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
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
public class StudentService implements RestService<Student, Long> {

    @Autowired
    StudentRepository repository;

    @Override
    public List<Student> getAll() {
        return repository.findAll();
    }

    @Override
    public Student getById(Long matriculationNumber) {
        Optional<Student> studentOptional = this.repository.findById(matriculationNumber);
        if (!studentOptional.isPresent()) throw new NotFoundException(matriculationNumber);
        return studentOptional.get();
    }

    @Override
    public void save(Student student) throws IllegalArgumentException {
        if (this.repository.existsById(student.getMatriculationNumber())) {
            log.info(String.format("FAIL: Student %s not created", student));
            throw new NotCreatedException(student);
        }
        repository.save(student);
        log.info(String.format("SUCCESS: Student %s created", student));
    }

    @Override
    public void delete(Long matriculationNumber) throws IllegalArgumentException {
        this.repository.delete(this.getById(matriculationNumber));
    }

    @Override
    public boolean update(Long matriculationNumber, Student update) {
        Student student = this.getById(matriculationNumber);

        if (!Objects.equals(student.getMatriculationNumber(), update.getMatriculationNumber())) {
            throw new StudentNotUpdatedException();
        }

        log.info("Updating student..");
        log.info(student.toString() + " -> " + update.toString());
        student.setTimeslots(update.getTimeslots());
        student.setUsername(update.getUsername());

        this.save(student);

        return true;
    }

}
