package de.hsrm.mi.swtp.exchangeplatform.model.serializer.admin.po;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.admin.po.violation.ProgressiveRegulationViolationMessage;

import java.io.IOException;
import java.util.ArrayList;

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
		gen.writeObjectField("modules_not_allowed", value.getModulesNotAllowed());
		gen.writeEndObject();
		gen.writeEndObject();
	}
	
}
