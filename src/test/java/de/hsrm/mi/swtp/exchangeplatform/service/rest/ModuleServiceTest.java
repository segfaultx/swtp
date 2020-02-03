package de.hsrm.mi.swtp.exchangeplatform.service.rest;


import de.hsrm.mi.swtp.exchangeplatform.exceptions.UserIsAlreadyAttendeeException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.messaging.sender.PersonalMessageSender;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.repository.ModuleRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ModuleServiceTest {
	
	@Mock
	private ModuleRepository repository;
	
	@Mock
	private ModuleLookupService moduleLookupService;
	
	@Mock
	private TimeslotService timeslotService;
	
	@Mock
	private PersonalMessageSender sender;
	
	@Mock
	private TradeOfferService tradeOfferService;

	@Spy
	@InjectMocks
	private ModuleService moduleService;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testAddAttendeeToModuleStudentIsAlreadyAttendee() {
	
		Module module = mock(Module.class);
		User user = mock(User.class);
		
		List<User> attendees = new ArrayList<>();
		attendees.add(user);
		
		when(module.getAttendees()).thenReturn(attendees);
		
		// We don't need the student number for this test
		when(user.getStudentNumber()).thenReturn(1L);
		
		assertThrows(UserIsAlreadyAttendeeException.class, () -> {
			moduleService.addAttendeeToModule(module, user);
		});
	}
	
	@Test
	public void testAddAttendeeToModuleUserSuccessfullyAddedToList() {
		Module module = mock(Module.class);
		User user = mock(User.class);
		
		List<User> attendees = new ArrayList<>();
		
		when(module.getAttendees()).thenReturn(attendees);
		
		moduleService.addAttendeeToModule(module, user);
		
		assertEquals(1, attendees.size());
		assertEquals(user, attendees.get(0));
	}
	
	@Test
	public void testRemoveStudentFromModule() throws NotFoundException {
		Module module = mock(Module.class);
		User user = mock(User.class);
		
		List<User> attendees = new ArrayList<>();
		attendees.add(user);
		
		when(module.getAttendees()).thenReturn(attendees);
		when(module.getTimeslots()).thenReturn(new ArrayList<>());
		
		moduleService.removeStudentFromModule(module, user);
		
		assertEquals(0, attendees.size());
	}
	
	@Test
	public void testRemoveStudentFromModuleAllDependentTimeslotsRemoved() throws NotFoundException {
		Module module = mock(Module.class);
		User user = mock(User.class);
		Timeslot timeslot1 = mock(Timeslot.class);
		Timeslot timeslot2 = mock(Timeslot.class);
		
		List<User> attendees = new ArrayList<>();
		attendees.add(user);
		
		List<Timeslot> timeslots = new ArrayList<>();
		timeslots.add(timeslot1);
		timeslots.add(timeslot2);
		
		List<User> timeslot1Attendees = new ArrayList<>();
		timeslot1Attendees.add(user);
		
		List<User> timeslot2Attendees = new ArrayList<>();

		when(module.getTimeslots()).thenReturn(timeslots);
		
		when(timeslot1.getAttendees()).thenReturn(timeslot1Attendees);
		when(timeslot2.getAttendees()).thenReturn(timeslot2Attendees);
		
		when(module.getAttendees()).thenReturn(attendees);
		
		doReturn(new Timeslot()).when(timeslotService).removeAttendeeFromTimeslot(timeslot1, user);
		
		moduleService.removeStudentFromModule(module, user);
		
		assertNotNull(timeslotService.removeAttendeeFromTimeslot(timeslot1, user));
		
		// Verify that removeAttendeeFromTimeslot was called for List where user was in, but not for second List
		verify(timeslotService, times(1)).removeAttendeeFromTimeslot(timeslot1, user);
		verify(timeslotService, times(0)).removeAttendeeFromTimeslot(timeslot2, user);
	}
}
