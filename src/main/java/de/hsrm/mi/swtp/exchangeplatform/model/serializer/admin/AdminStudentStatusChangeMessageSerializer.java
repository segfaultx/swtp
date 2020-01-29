package de.hsrm.mi.swtp.exchangeplatform.model.serializer.admin;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.admin.AdminStudentStatusChangeMessage;

import java.io.IOException;

public class AdminStudentStatusChangeMessageSerializer extends StdSerializer<AdminStudentStatusChangeMessage> {
	
	public AdminStudentStatusChangeMessageSerializer() {
		this(null);
	}
	
	public AdminStudentStatusChangeMessageSerializer(Class<AdminStudentStatusChangeMessage> t) {
		super(t);
	}
	
	@Override
	public void serialize(AdminStudentStatusChangeMessage value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeStartObject();
		gen.writeStringField("type", value.getMessageType().toString());
		gen.writeStringField("message", value.getMessage());
		gen.writeFieldName("value");
		gen.writeStartObject();
		gen.writeObjectField("student", value.getStudent());
		gen.writeEndObject();
		gen.writeEndObject();
	}
	
}
