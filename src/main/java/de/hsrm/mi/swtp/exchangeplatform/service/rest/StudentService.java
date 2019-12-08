package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.StudentNotUpdatedException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notcreated.NotCreatedException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.messaging.sender.StudentMessageSender;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Student;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.repository.StudentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StudentService implements RestService<Student, Long> {
	
	StudentRepository repository;
	StudentMessageSender messageSender;
	
	@Override
	public List<Student> getAll() {
		List<Student> students = repository.findAll();
		
		// Kill bidirectional infinite recursion by setting timeslots attendees and module timeslots to null
		for(Student student : students) {
			List<Timeslot> timeslots = student.getTimeslots();
			
			for(Timeslot timeslot : timeslots) {
				timeslot.setAttendees(null);
				//timeslot.getModule().setTimeslots(null);
			}
		}
		return students;
	}
	
	@Override
	public Student getById(Long studentId) {
		Optional<Student> studentOptional = this.repository.findById(studentId);
		if(!studentOptional.isPresent()) {
			log.info(String.format("FAIL: Student %s not found", studentId));
			throw new NotFoundException(studentId);
		}
		return studentOptional.get();
	}
	
	public Student getByUsername(String username) {
		Optional<Student> studentOptional = this.repository.findByUsername(username);
		return studentOptional.orElseThrow(() -> new NotFoundException("Student not found"));
	}
	
	@Override
	public void save(Student student) throws IllegalArgumentException {
		if(this.repository.existsById(student.getStudentId())) {
			log.info(String.format("FAIL: Student %s not created. Student already exists", student));
			throw new NotCreatedException(student);
		}
		repository.save(student);
		log.info(String.format("SUCCESS: Student %s created", student));
		messageSender.send(student);
	}
	
	@Override
	public void delete(Long studentId) throws IllegalArgumentException {
		this.repository.delete(this.getById(studentId));
	}
	
	@Override
	public boolean update(Long studentId, Student update) {
		Student student = this.getById(studentId);
		
		if(!Objects.equals(student.getStudentId(), update.getStudentId())) {
			log.info(String.format("FAIL: Something went wrong. Student %s not found.", studentId));
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
