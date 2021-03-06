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

//TODO: javadoc
public class TimeslotSerializer extends StdSerializer<Timeslot> {
	
	public TimeslotSerializer() {
		this(null);
	}
	
	public TimeslotSerializer(Class<Timeslot> t) {
		super(t);
	}
	
	@Override
	public void serialize(Timeslot value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeStartObject();
		gen.writeNumberField("id", value.getId());
		gen.writeObjectField("room", value.getRoom());
		gen.writeObjectField("day", value.getDay());
		gen.writeNumberField("capacity", value.getCapacity());
		gen.writeStringField("time_start", value.getTimeStart().toString());
		gen.writeStringField("time_end", value.getTimeEnd().toString());
		gen.writeStringField("timeslot_type", value.getTimeSlotType().name());
		gen.writeBooleanField("is_tradeable", value.getIsTradeable());
		gen.writeStringField("group", value.getPracticalGroup());
		gen.writeStringField("user_initials", value.getUser().getInitials());
		
		final TimeslotModuleSerializer timeslotModuleSerializer = new TimeslotModuleSerializer();
		gen.writeFieldName("module");
		timeslotModuleSerializer.serialize(value.getModule(), gen, provider);
		
		gen.writeFieldName("attendees");
		final List<User> students = value.getAttendees()
								   .stream()
								   .filter((User user) -> user.getUserType().getType().equals(TypeOfUsers.STUDENT))
								   .collect(Collectors.toList());
		gen.writeStartArray();
		for(User student: students) gen.writeNumber(student.getStudentNumber());
		gen.writeEndArray();
		
		gen.writeObjectField("waitlist", value.getWaitList()
											  .stream()
											  .map(user -> user.getId())
											  .collect(Collectors.toList()));
		
		gen.writeEndObject();
		
	}
}
