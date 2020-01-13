package de.hsrm.mi.swtp.exchangeplatform.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.TypeOfUsers;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class TimeslotSerializer extends StdSerializer<Timeslot> {
	
	public TimeslotSerializer() {
		this(null);
	}
	
	public TimeslotSerializer(Class<Timeslot> t) {
		super(t);
	}
	
	@Override
	public void serialize(Timeslot value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		// TODO: Prüfen, ob custom serialisierer noch benötigt wird
		gen.writeStartObject();
		gen.writeNumberField("id", value.getId());
		gen.writeObjectField("room", value.getRoom());
		gen.writeObjectField("day", value.getDay());
		gen.writeNumberField("capacity", value.getCapacity());
		gen.writeObjectField("time_start", value.getTimeStart());
		gen.writeObjectField("time_end", value.getTimeEnd());
		gen.writeNumberField("module_id", value.getModule().getId());
		gen.writeFieldName("attendees");
		final List<User> students = value.getAttendees()
								   .stream()
								   .filter((User user) -> user.getUserType().getType().equals(TypeOfUsers.STUDENT))
								   .collect(Collectors.toList());
		gen.writeStartArray();
		for(User student: students) gen.writeNumber(student.getStudentNumber());
		gen.writeEndArray();
		gen.writeEndObject();
		
	}
}
