package de.hsrm.mi.swtp.exchangeplatform.service.filter;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.DayOfWeek;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.TypeOfTimeslots;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.TradeFilter.CapacityFilter;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.TradeFilter.CollisionFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FilterTest {
	
	private List<Timeslot> timeslots = new ArrayList<>();
	private List<TradeOffer> tradeOffers = new ArrayList<>();
	private List<User> attendees = new ArrayList<>();
	
	@BeforeEach
	public void setup(){
		
		User u1 = mock(User.class);
		User u2 = mock(User.class);
		User u3 = mock(User.class);
		attendees = List.of(u1,u2,u3);
		
		//offer
		Timeslot t1 = mock(Timeslot.class);
		Mockito.when(t1.getTimeSlotType()).thenReturn(TypeOfTimeslots.PRAKTIKUM);
		Mockito.when(t1.getTimeStart()).thenReturn(LocalTime.of(8, 0));
		Mockito.when(t1.getTimeEnd()).thenReturn(LocalTime.of(10, 0));
		Mockito.when(t1.getDay()).thenReturn(DayOfWeek.MONDAY);
		Mockito.when(t1.getCapacity()).thenReturn(20);
		Mockito.when(t1.getAttendees()).thenReturn(attendees);
		
		Timeslot t2 = mock(Timeslot.class);
		Mockito.when(t2.getTimeSlotType()).thenReturn(TypeOfTimeslots.PRAKTIKUM);
		Mockito.when(t2.getTimeStart()).thenReturn(LocalTime.of(13, 0));
		Mockito.when(t2.getTimeEnd()).thenReturn(LocalTime.of(14, 0));
		Mockito.when(t2.getDay()).thenReturn(DayOfWeek.MONDAY);
		Mockito.when(t2.getCapacity()).thenReturn(20);
		Mockito.when(t2.getAttendees()).thenReturn(attendees);
		
		Timeslot t3 = mock(Timeslot.class);
		Mockito.when(t3.getTimeSlotType()).thenReturn(TypeOfTimeslots.VORLESUNG);
		Mockito.when(t3.getTimeStart()).thenReturn(LocalTime.of(8, 0));
		Mockito.when(t3.getTimeEnd()).thenReturn(LocalTime.of(16, 0));
		Mockito.when(t3.getDay()).thenReturn(DayOfWeek.MONDAY);
		Mockito.when(t3.getCapacity()).thenReturn(20);
		Mockito.when(t3.getAttendees()).thenReturn(attendees);
		
		// seek
		Timeslot t4 = mock(Timeslot.class);
		Mockito.when(t4.getTimeSlotType()).thenReturn(TypeOfTimeslots.PRAKTIKUM);
		Mockito.when(t4.getTimeStart()).thenReturn(LocalTime.of(8, 0));
		Mockito.when(t4.getTimeEnd()).thenReturn(LocalTime.of(9, 45));
		Mockito.when(t4.getDay()).thenReturn(DayOfWeek.MONDAY);
		Mockito.when(t4.getCapacity()).thenReturn(20);
		Mockito.when(t4.getAttendees()).thenReturn(attendees);
		
		Timeslot t5 = mock(Timeslot.class);
		Mockito.when(t5.getTimeSlotType()).thenReturn(TypeOfTimeslots.PRAKTIKUM);
		Mockito.when(t5.getTimeStart()).thenReturn(LocalTime.of(20, 0));
		Mockito.when(t5.getTimeEnd()).thenReturn(LocalTime.of(21, 0));
		Mockito.when(t5.getDay()).thenReturn(DayOfWeek.MONDAY);
		Mockito.when(t5.getCapacity()).thenReturn(2);
		Mockito.when(t5.getAttendees()).thenReturn(attendees);
		
		Timeslot t6 = mock(Timeslot.class);
		Mockito.when(t6.getTimeSlotType()).thenReturn(TypeOfTimeslots.PRAKTIKUM);
		Mockito.when(t6.getTimeStart()).thenReturn(LocalTime.of(11, 0));
		Mockito.when(t6.getTimeEnd()).thenReturn(LocalTime.of(12, 0));
		Mockito.when(t6.getDay()).thenReturn(DayOfWeek.MONDAY);
		Mockito.when(t6.getCapacity()).thenReturn(20);
		Mockito.when(t6.getAttendees()).thenReturn(attendees);
		
		TradeOffer to1 = mock(TradeOffer.class);
		Mockito.when(to1.getSeek()).thenReturn(t1);
		Mockito.when(to1.getOffer()).thenReturn(t4);
		TradeOffer to2 = mock(TradeOffer.class);
		Mockito.when(to2.getSeek()).thenReturn(t2);
		Mockito.when(to2.getOffer()).thenReturn(t5);
		TradeOffer to3 = mock(TradeOffer.class);
		Mockito.when(to3.getSeek()).thenReturn(t3);
		Mockito.when(to3.getOffer()).thenReturn(t6);
		tradeOffers = List.of(to1, to2, to3);
		timeslots = List.of(t1,t2,t3,t4,t5,t6);
		
		
	}
	
	@Test
	public void testCollision(){
		User seeker = mock(User.class);
		Mockito.when(seeker.getTimeslots()).thenReturn(List.of(timeslots.get(3)));
		Filter collision = new CollisionFilter();
		
		List<TradeOffer> resultOffer = collision.doFilter(tradeOffers, seeker);
		List<TradeOffer> expectedOffer = List.of(tradeOffers.get(1),tradeOffers.get(2));
		assertEquals(resultOffer, expectedOffer);
	}
	
	@Test
	public void testCapacity(){
		User seeker = mock(User.class);
		Filter capacity = new CapacityFilter();
		
		List<TradeOffer> result = capacity.doFilter(tradeOffers, seeker);
		List<TradeOffer> expected = List.of(tradeOffers.get(0),tradeOffers.get(2));
	}
	
	
	
}
