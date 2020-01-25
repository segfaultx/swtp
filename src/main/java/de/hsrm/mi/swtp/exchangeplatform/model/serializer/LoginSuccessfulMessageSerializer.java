package de.hsrm.mi.swtp.exchangeplatform.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.LoginSuccessfulMessage;

import java.io.IOException;

public class LoginSuccessfulMessageSerializer extends StdSerializer<LoginSuccessfulMessage> {
	
	public LoginSuccessfulMessageSerializer(Class<LoginSuccessfulMessage> t) {
		super(t);
	}
	
	@Override
	public void serialize(LoginSuccessfulMessage value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeStartObject();
		gen.writeStringField("message", value.getMessage());
		gen.writeFieldName("value");
		gen.writeStartObject();
		gen.writeStringField("timestamp", value.getTimestampString());
		gen.writeEndObject();
		gen.writeEndObject();
	}
	
}
