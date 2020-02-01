package de.hsrm.mi.swtp.exchangeplatform.model.admin.po;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.admin.po.violation.UserOccupancyViolationMessage;

import java.io.IOException;

public class UserOccupancyViolationSerializer extends StdSerializer<UserOccupancyViolationMessage> {
	
	public UserOccupancyViolationSerializer() {
		this(null);
	}
	
	public UserOccupancyViolationSerializer(Class<UserOccupancyViolationMessage> t) {
		super(t);
	}
	
	@Override
	public void serialize(UserOccupancyViolationMessage value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeStartObject();
		gen.writeStringField("type", value.getMessageType().toString());
		gen.writeStringField("message", value.getMessage());
		gen.writeFieldName("value");
		gen.writeStartObject();
		gen.writeArrayFieldStart("violations");
		for(Object violation : value.getViolations().values()) gen.writeObject(violation);
		gen.writeEndArray();
		gen.writeStringField("student_id", value.getStudent().getAuthenticationInformation().getUsername());
		gen.writeEndObject();
		gen.writeEndObject();
	}
}