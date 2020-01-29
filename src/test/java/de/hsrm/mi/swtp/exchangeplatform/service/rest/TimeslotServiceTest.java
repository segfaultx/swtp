package de.hsrm.mi.swtp.exchangeplatform.service.rest;


import de.hsrm.mi.swtp.exchangeplatform.exceptions.NoTimeslotCapacityException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.UserIsAlreadyAttendeeException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext
public class TimeslotServiceTest {
	
	@Autowired
	private TimeslotService service;
	
	@Test
	public void testAddAttendeeToTimeslotUserIsAlreadyAttendee() {
		User user = mock(User.class);
		
		Timeslot timeslot = mock(Timeslot.class, RETURNS_DEEP_STUBS);
		
		when(timeslot.getAttendees().contains(user)).thenReturn(true);
		
		assertThrows(UserIsAlreadyAttendeeException.class, () -> {
			service.addAttendeeToTimeslot(timeslot, user);
		});
	}
	
	@Test
	public void testAddAttendeeToTimeslotCapacityReached() {
		User user = mock(User.class);
		
		Timeslot timeslot = mock(Timeslot.class, RETURNS_DEEP_STUBS);
		
		when(timeslot.getAttendees().contains(user)).thenReturn(false);
		
		// User and Timeslot IDs are not relevant for this test
		when(user.getStudentNumber()).thenReturn(1L);
		when(timeslot.getId()).thenReturn(1L);
		
		when(timeslot.getCapacity()).thenReturn(1);
		List<User> userList = List.of(mock(User.class), mock(User.class));
		
		when(timeslot.getAttendees()).thenReturn(userList);
		
		assertThrows(NoTimeslotCapacityException.class, () -> {
			service.addAttendeeToTimeslot(timeslot, user);
		});
	}
	
	@Test
	public void testAddAttendeeToTimeslotAddingIsSuccessful() {
	
	
	
//		User user = mock(User.class);
//
//		Timeslot timeslot = mock(Timeslot.class);
//
//		TimeslotService service = mock(TimeslotService.class);
//
//		when(service.addAttendeeToTimeslot(timeslot, user)).thenCallRealMethod();
//
//		List<User> userList = doReturn(new ArrayList<>()).when(timeslot.getAttendees());
//		when(userList.contains(user)).thenReturn(false);
//
//		// User and Timeslot IDs are not relevant for this test
//		when(user.getStudentNumber()).thenReturn(1L);
//		when(timeslot.getId()).thenReturn(1L);
//
//		when(service.checkCapacity(timeslot)).thenReturn(true);
//
//		assertEquals(service.addAttendeeToTimeslot(timeslot, user), timeslot);
	}
	
}
