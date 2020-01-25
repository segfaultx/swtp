package de.hsrm.mi.swtp.exchangeplatform.model.admin.po;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.hsrm.mi.swtp.exchangeplatform.service.admin.po.filter.UserOccupancyViolation;

import java.io.IOException;

public class UserOccupancyViolationSerializer extends StdSerializer<UserOccupancyViolation> {
	
	public UserOccupancyViolationSerializer() {
		this(null);
	}
	
	public UserOccupancyViolationSerializer(Class<UserOccupancyViolation> t) {
		super(t);
	}
	
	@Override
	public void serialize(UserOccupancyViolation value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeStartObject();
		gen.writeStringField("type", value.getMessageType().toString());
		gen.writeStringField("message", value.getMessage());
		gen.writeFieldName("value");
		gen.writeStartObject();
		gen.writeObjectField("violations", value.getViolations());
		gen.writeStringField("student_id", value.getStudent().getAuthenticationInformation().getUsername());
		gen.writeEndObject();
		gen.writeEndObject();
	}
}