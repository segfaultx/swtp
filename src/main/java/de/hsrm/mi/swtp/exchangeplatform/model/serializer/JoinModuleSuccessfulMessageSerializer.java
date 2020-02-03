package de.hsrm.mi.swtp.exchangeplatform.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.JoinModuleSuccessfulMessage;

import java.io.IOException;

public class JoinModuleSuccessfulMessageSerializer extends StdSerializer<JoinModuleSuccessfulMessage> {
	
	public JoinModuleSuccessfulMessageSerializer() {
		this(null);
	}
	
	public JoinModuleSuccessfulMessageSerializer(Class<JoinModuleSuccessfulMessage> t) {
		super(t);
	}
	
	@Override
	public void serialize(JoinModuleSuccessfulMessage value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeStartObject();
		gen.writeStringField("type", value.getMessageType().toString());
		gen.writeStringField("message", value.getMessage());
		gen.writeFieldName("value");
		gen.writeStartObject();
		gen.writeStringField("timestamp", value.getTime().toString());
		gen.writeNumberField("module_id", value.getModule().getId());
		gen.writeEndObject();
		gen.writeEndObject();
	}
	
}
