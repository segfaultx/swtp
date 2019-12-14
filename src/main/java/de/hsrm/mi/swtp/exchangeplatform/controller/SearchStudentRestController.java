package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Student;
import de.hsrm.mi.swtp.exchangeplatform.repository.StudentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RestController
@RequestMapping("/api/v1/student")
public class SearchStudentRestController{
	
	String BASEURL = "/api/v1/searchStudent";
	StudentRepository studentRepository;
	
	@GetMapping("")
	public ResponseEntity<List<Student>> getAllStudents() {
		log.info("GET // " + BASEURL);
		return new ResponseEntity<>(studentRepository.findAll(), HttpStatus.OK);
	}
	
	
	@GetMapping("/{searchParameter}")
	public ResponseEntity<List<Student>> getSearchResult(
			@Or({
				@Spec(path = "firstName", pathVars = "searchParameter", spec = Like.class),
				@Spec(path = "lastName", pathVars = "searchParameter", spec = Like.class),
				@Spec(path = "id", pathVars = "searchParameter", spec = Like.class)})
			Specification<Student> studentSpecification){
		//log.info(String.format("GET // " + BASEURL + "/%s", searchParameter));
		try {
			return new ResponseEntity<>((List<Student>) studentRepository.findAll((Pageable) studentSpecification),HttpStatus.OK);
		} catch(NotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
}
