package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.TypeOfUsers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BasicTradeServiceTest {
	
	private BasicTradeService tradeService = mock(BasicTradeService.class);
	
	@Test
	public void testDoTradeWithOneLecturer() {
		User user1 = mock(User.class, RETURNS_DEEP_STUBS);
		User user2 = mock(User.class, RETURNS_DEEP_STUBS);
		
		Timeslot timeslot = mock(Timeslot.class);
		Timeslot timeslot2 = mock(Timeslot.class);

		when(tradeService.doTrade(user1, user2, timeslot, timeslot2)).thenCallRealMethod();

		when(user1.getUserType().getType()).thenReturn(TypeOfUsers.STUDENT);
		when(user2.getUserType().getType()).thenReturn(TypeOfUsers.LECTURER);
		
		boolean actual = tradeService.doTrade(user1, user2, timeslot, timeslot2);
		assertFalse("One is a lecturer", actual);
	}
}
