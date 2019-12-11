package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.NotUpdatedException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notcreated.NotCreatedException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Student;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.StudentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RestController
@RequestMapping("/api/v1/student")
public class StudentRestController implements BaseRestController<Student, Long> {
	
	String BASEURL = "/api/v1/student";
	StudentService studentService;
	
	@Override
	public ResponseEntity<List<Student>> getAll() {
		log.info("GET // " + BASEURL);
		return new ResponseEntity<>(studentService.getAll(), HttpStatus.OK);
	}
	
	@Override
	@GetMapping("/{studentId}")
	public ResponseEntity<Student> getById(@PathVariable Long studentId) {
		log.info(String.format("GET // " + BASEURL + "/%s", studentId));
		try {
			return new ResponseEntity<>(studentService.getById(studentId), HttpStatus.OK);
		} catch(NotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@Override
	public ResponseEntity<Student> create(@RequestBody Student student, BindingResult result) {
		log.info(String.format("POST // " + BASEURL + "/%s", student.toString()));
		if(result.hasErrors()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		try {
			studentService.save(student);
			log.info(String.format("SUCCESS: Created new student %s", student.getStudentId()));
			return new ResponseEntity<>(student, HttpStatus.OK);
		} catch(NotCreatedException e) {
			log.info(String.format("FAIL: Student %s not created", student.getStudentId()));
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch(IllegalArgumentException e) {
			log.info(String.format("FAIL: Student %s not created due to some error", student.getStudentId()));
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@Override
	@PatchMapping("/{studentId}")
	public ResponseEntity<Student> update(@PathVariable Long studentId, @RequestBody Student student, BindingResult result) {
		log.info(String.format("PATCH // " + BASEURL + "/%s", studentId));
		if(result.hasErrors()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		try {
			studentService.update(studentId, student);
			log.info(String.format("SUCCESS: Updated student: id=%s", studentId));
			return new ResponseEntity<>(studentService.getById(studentId), HttpStatus.OK);
		} catch(NotFoundException e) {
			log.info(String.format("FAIL: Student %s not found", studentId));
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch(NotUpdatedException e) {
			return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
		}
		
	}
	
	@Override
	@DeleteMapping("/admin/{studentId}")
	public ResponseEntity<Student> delete(@PathVariable Long studentId) {
		log.info(String.format("DELETE // " + BASEURL + "/admin/%s", studentId));
		
		try {
			this.studentService.delete(studentId);
			log.info(String.format("SUCCESS: Deleted student %s", studentId));
			return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
		} catch(NotFoundException e) {
			log.info(String.format("FAIL: Student %s not found", studentId));
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch(IllegalArgumentException e) {
			log.info(String.format("FAIL: Something went wrong during deletion of student %s", studentId));
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
	}
	
	
}
