/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (4.2.1).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.model.rest_models.Student;
import de.hsrm.mi.swtp.exchangeplatform.model.rest_models.Timetable;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-12-04T10:25:24.919398+01:00[Europe/Berlin]")

@Validated
@Api(value = "students", description = "the students API")
public interface StudentsApi {
	
	@ApiOperation(value = "Get personalized Timetable of student.", nickname = "getPersonalizedTimetable", notes = "", response = Timetable.class, responseContainer = "List", tags = { })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "a timetable", response = Timetable.class, responseContainer = "List") })
	@RequestMapping(value = "/students/{studentId}/personalizedTimetable", produces = { "application/json" }, method = RequestMethod.GET)
	ResponseEntity<List<Timetable>> getPersonalizedTimetable(
			@ApiParam(value = "Numeric ID of the student", required = true) @PathVariable("studentId") Long studentId
															);
	
	
	@ApiOperation(value = "Returns detailed information about the student specified by the Id.", nickname = "getStudentById", notes = "", response = Student.class, tags = { })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "A single student", response = Student.class) })
	@RequestMapping(value = "/students/{studentId}", produces = { "application/json" }, method = RequestMethod.GET)
	ResponseEntity<Student> getStudentById(@ApiParam(value = "Numeric ID of the student to get", required = true) @PathVariable("studentId") Long studentId);
	
}
