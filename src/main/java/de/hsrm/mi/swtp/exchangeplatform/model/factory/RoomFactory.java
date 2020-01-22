package de.hsrm.mi.swtp.exchangeplatform.model.factory;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Room;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple factory for creating a {@link Room} instance.
 */
@Component("userFactory")
public class RoomFactory {
	
	@Value("${exchangeplatform.default.room.location}")
	private static String DEFAULT_LOCATION;
	
	/** @see RoomFactory */
	public static Room createRoom(final String roomNumber) {
		return createRoom(roomNumber, DEFAULT_LOCATION);
	}
	
	/** @see RoomFactory */
	public static Room createRoom(final String roomNumber,
								  final String location) {
		return createRoom(roomNumber, location, new ArrayList<>());
	}
	
	
	/** @see RoomFactory */
	public static Room createRoom(final String roomNumber,
								  final String location,
								  @NonNull final List<Timeslot> timeslots) {
		Room room = new Room();
		room.setRoomNumber(roomNumber);
		room.setLocation(location);
		room.setTimeslots(timeslots);
		
		return room;
	}
	
}
