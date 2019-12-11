package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Room;
import de.hsrm.mi.swtp.exchangeplatform.model.rest_models.RoomDTO;
import org.springframework.stereotype.Service;

@Service
public class RoomRestModelConverter implements RestModelConverter<RoomDTO> {
	@Override
	public boolean isResponsible(Object object) {
		return object instanceof Room;
	}
	
	@Override
	public RoomDTO convertToRest(Object object) {
		RoomDTO out = new RoomDTO();
		Room room = (Room) object;
		out.setId(room.getId());
		out.setLocation(room.getLocation());
		out.setRoomNumber(room.getRoomNumber());
		return out;
	}
}
