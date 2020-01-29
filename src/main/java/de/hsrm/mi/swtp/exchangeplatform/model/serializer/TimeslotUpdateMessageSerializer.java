package de.hsrm.mi.swtp.exchangeplatform.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.TimeslotUpdateMessage;

import java.io.IOException;

public class TimeslotUpdateMessageSerializer extends StdSerializer<TimeslotUpdateMessage> {
	
	public TimeslotUpdateMessageSerializer() {
		this(null);
	}
	
	public TimeslotUpdateMessageSerializer(Class<TimeslotUpdateMessage> t) {
		super(t);
	}
	
	@Override
	public void serialize(TimeslotUpdateMessage value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeStartObject();
		gen.writeStringField("type", value.getMessageType().toString());
		gen.writeStringField("message", value.getMessage());
		gen.writeObjectField("value", value.getTimeslot());
		gen.writeEndObject();
	}
	
}
