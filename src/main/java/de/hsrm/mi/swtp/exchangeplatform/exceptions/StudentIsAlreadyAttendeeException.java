package de.hsrm.mi.swtp.exchangeplatform.exceptions;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Student;

public class StudentIsAlreadyAttendeeException extends RuntimeException {
	
	public StudentIsAlreadyAttendeeException(Student student) {
		super(String.format("Student %s is already an attendee.", student.getUsername() + ", " + student.getStudentId()));
	}
}
