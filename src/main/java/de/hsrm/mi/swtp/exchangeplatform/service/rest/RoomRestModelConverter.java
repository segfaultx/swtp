package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Room;
import org.springframework.stereotype.Service;

@Service
public class RoomRestModelConverter implements RestModelConverter<de.hsrm.mi.swtp.exchangeplatform.model.rest_models.Room> {
	@Override
	public boolean isResponsible(Object object) {
		return object instanceof Room;
	}
	
	@Override
	public de.hsrm.mi.swtp.exchangeplatform.model.rest_models.Room convertToRest(Object object) {
		de.hsrm.mi.swtp.exchangeplatform.model.rest_models.Room out = new de.hsrm.mi.swtp.exchangeplatform.model.rest_models.Room();
		Room room = (Room) object;
		out.setId(room.getId());
		out.setLocation(room.getLocation());
		out.setRoomNumber(room.getRoomNumber());
		return out;
	}
}
