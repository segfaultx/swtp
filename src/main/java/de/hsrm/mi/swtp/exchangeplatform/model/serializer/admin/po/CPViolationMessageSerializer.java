package de.hsrm.mi.swtp.exchangeplatform.model.serializer.admin.po;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.admin.po.violation.CPViolationMessage;

import java.io.IOException;

public class CPViolationMessageSerializer extends StdSerializer<CPViolationMessage> {
	
	public CPViolationMessageSerializer() {
		this(null);
	}
	
	public CPViolationMessageSerializer(Class<CPViolationMessage> t) {
		super(t);
	}
	
	@Override
	public void serialize(CPViolationMessage value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeStartObject();
		gen.writeStringField("type", value.getMessageType().toString());
		gen.writeStringField("message", value.getMessage());
		gen.writeFieldName("value");
		gen.writeStartObject();
		gen.writeObjectField("max_cp", value.getMaxCPByPO());
		gen.writeObjectField("occupied_cp", value.getUserCP());
		gen.writeEndObject();
		gen.writeEndObject();
	}
	
}