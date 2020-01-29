package de.hsrm.mi.swtp.exchangeplatform.service.rest;


import de.hsrm.mi.swtp.exchangeplatform.exceptions.NoTimeslotCapacityException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.UserIsAlreadyAttendeeException;
import de.hsrm.mi.swtp.exchangeplatform.messaging.sender.AdminTopicMessageSender;
import de.hsrm.mi.swtp.exchangeplatform.messaging.sender.TimeslotTopicMessageSender;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.repository.TimeslotRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.UserRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext
public class TimeslotServiceTest {
	
	@Mock
	private TimeslotTopicMessageSender topicMessageSender;
	
	@Mock
	private AdminTopicMessageSender adminTopicMessageSender;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private TimeslotRepository repository;
	
	@InjectMocks
	private TimeslotService service;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
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
		
		List<User> mockedList = mock(List.class);
		List<User> userList = List.of(mock(User.class), mock(User.class));
		User user = mock(User.class, RETURNS_DEEP_STUBS);
		Timeslot timeslot = mock(Timeslot.class);
		
		when(timeslot.getAttendees()).thenReturn(mockedList);
		when(mockedList.contains(user)).thenReturn(false);
		
		// User and Timeslot IDs are not relevant for this test
		when(user.getStudentNumber()).thenReturn(1L);
		when(timeslot.getId()).thenReturn(1L);
		when(timeslot.getCapacity()).thenReturn(3);
		
		when(timeslot.getAttendees()).thenReturn(userList);
		when(repository.save(timeslot)).thenReturn(timeslot);
		when(userRepository.findByStudentNumber(user.getStudentNumber())).thenReturn(mock(User.class));
		doNothing().when(adminTopicMessageSender).send(any());
		doNothing().when(topicMessageSender).notifyAll(any());
		
		Timeslot savedTimeslot = service.addAttendeeToTimeslot(timeslot, user);
		assertNotNull(savedTimeslot);
	}
	
}
