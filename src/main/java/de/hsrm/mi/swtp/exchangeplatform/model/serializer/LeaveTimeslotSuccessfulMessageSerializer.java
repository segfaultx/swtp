package de.hsrm.mi.swtp.exchangeplatform.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.LeaveTimeslotSuccessfulMessage;

import java.io.IOException;

public class LeaveTimeslotSuccessfulMessageSerializer extends StdSerializer<LeaveTimeslotSuccessfulMessage> {
	
	public LeaveTimeslotSuccessfulMessageSerializer() {
		this(null);
	}
	
	public LeaveTimeslotSuccessfulMessageSerializer(Class<LeaveTimeslotSuccessfulMessage> t) {
		super(t);
	}
	
	@Override
	public void serialize(LeaveTimeslotSuccessfulMessage value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeStartObject();
		gen.writeStringField("type", value.getMessageType().toString());
		gen.writeStringField("message", value.getMessage());
		gen.writeFieldName("value");
		gen.writeStartObject();
		gen.writeStringField("timestamp", value.getTime().toString());
		gen.writeNumberField("timeslot_id", value.getTimeslot().getId());
		gen.writeEndObject();
		gen.writeEndObject();
	}
	
}
