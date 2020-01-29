package de.hsrm.mi.swtp.exchangeplatform.model.serializer.admin.po;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.admin.po.violation.DualPOViolationMessage;

import java.io.IOException;

public class DualPOViolationMessageSerializer extends StdSerializer<DualPOViolationMessage> {
	
	public DualPOViolationMessageSerializer() {
		this(null);
	}
	
	public DualPOViolationMessageSerializer(Class<DualPOViolationMessage> t) {
		super(t);
	}
	
	@Override
	public void serialize(DualPOViolationMessage value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeStartObject();
		gen.writeStringField("type", value.getMessageType().toString());
		gen.writeStringField("message", value.getMessage());
		gen.writeFieldName("value");
		gen.writeStartObject();
		gen.writeObjectField("blocked_day", value.getBlockedDay());
		gen.writeObjectField("timeslots_not_allowed", value.getTimeslotsNotAllowed());
		gen.writeEndObject();
		gen.writeEndObject();
	}
	
}