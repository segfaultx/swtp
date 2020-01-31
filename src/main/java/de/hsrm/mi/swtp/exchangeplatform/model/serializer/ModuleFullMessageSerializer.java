package de.hsrm.mi.swtp.exchangeplatform.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.ModuleFullMessage;

import java.io.IOException;

//TODO: javadoc
public class ModuleFullMessageSerializer extends StdSerializer<ModuleFullMessage> {
	
	public ModuleFullMessageSerializer() {
		this(null);
	}
	
	public ModuleFullMessageSerializer(Class<ModuleFullMessage> t) {
		super(t);
	}
	
	@Override
	public void serialize(ModuleFullMessage value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeStartObject();
		gen.writeStringField("type", value.getMessageType().toString());
		gen.writeStringField("message", value.getMessage());
		gen.writeObjectField("value", value.getModule());
		gen.writeEndObject();
	}
}
