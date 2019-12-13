package de.hsrm.mi.swtp.exchangeplatform.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;

import java.io.IOException;

public class TimeslotSerializer extends StdSerializer<Timeslot> {
	
	public TimeslotSerializer() {
		this(null);
	}
	
	public TimeslotSerializer(Class<Timeslot> t) {
		super(t);
	}
	
	@Override
	public void serialize(Timeslot value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		
		// TODO: Serialisierung auf Fehler testen und ggfs fixen
		gen.writeStartObject();
		gen.writeNumberField("id", value.getId());
		gen.writeObjectField("room", value.getRoom());
		gen.writeObjectField("day", value.getDay());
//		gen.writeObjectField("lecturer", value.getLecturer());
//		gen.writeStringField("type", value.getType());
		gen.writeNumberField("capacity", value.getCapacity());
		gen.writeFieldName("attendees");
		gen.writeStartArray();
//		for(Student student : value.getAttendees()) {
//			gen.writeNumber(student.getStudentId());
//		}
		gen.writeEndArray();
		gen.writeEndObject();
		
	}
}
