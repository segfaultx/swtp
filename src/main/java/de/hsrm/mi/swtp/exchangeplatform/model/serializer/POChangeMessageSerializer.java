package de.hsrm.mi.swtp.exchangeplatform.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.POChangeMessage;

import java.io.IOException;

public class POChangeMessageSerializer extends StdSerializer<POChangeMessage> {
	
	public POChangeMessageSerializer() {
		this(null);
	}
	
	public POChangeMessageSerializer(Class<POChangeMessage> t) {
		super(t);
	}
	
	@Override
	public void serialize(POChangeMessage value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeStartObject();
		gen.writeStringField("type", value.getMessageType().toString());
		gen.writeStringField("message", value.getMessage());
		gen.writeFieldName("value");
		gen.writeStartObject();
		gen.writeObjectField("po", value.getPo());
		gen.writeEndObject();
		gen.writeEndObject();
	}
	
}
