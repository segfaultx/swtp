package de.hsrm.mi.swtp.exchangeplatform.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.ProgressiveRegulationViolationMessage;

import java.io.IOException;

public class ProgressiveRegulationViolationMessageSerializer extends StdSerializer<ProgressiveRegulationViolationMessage> {
	
	public ProgressiveRegulationViolationMessageSerializer() {
		this(null);
	}
	
	public ProgressiveRegulationViolationMessageSerializer(Class<ProgressiveRegulationViolationMessage> t) {
		super(t);
	}
	
	@Override
	public void serialize(ProgressiveRegulationViolationMessage value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeStartObject();
		gen.writeStringField("type", value.getMessageType().name());
		gen.writeStringField("message", value.getMessage());
		gen.writeFieldName("value");
		gen.writeStartObject();
		gen.writeFieldName("not_allowed");
		gen.writeStartArray();
		for(Long id : value.getModulesNotAllowed()) {
			gen.writeNumber(id);
		}
		gen.writeEndArray();
		gen.writeEndObject();
		gen.writeEndObject();
	}
	
}
