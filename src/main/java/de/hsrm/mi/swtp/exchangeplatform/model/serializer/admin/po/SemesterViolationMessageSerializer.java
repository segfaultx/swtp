package de.hsrm.mi.swtp.exchangeplatform.model.serializer.admin.po;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.admin.po.violation.SemesterViolationMessage;

import java.io.IOException;

public class SemesterViolationMessageSerializer extends StdSerializer<SemesterViolationMessage> {
	
	public SemesterViolationMessageSerializer() {
		this(null);
	}
	
	public SemesterViolationMessageSerializer(Class<SemesterViolationMessage> t) {
		super(t);
	}
	
	@Override
	public void serialize(SemesterViolationMessage value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeStartObject();
		gen.writeStringField("type", value.getMessageType().toString());
		gen.writeStringField("message", value.getMessage());
		gen.writeFieldName("value");
		gen.writeStartObject();
		gen.writeObjectField("min_semester", value.getPoSemester());
		gen.writeObjectField("modules_missing", value.getMissingModules());
		gen.writeObjectField("modules_not_allowed", value.getModulesNotAllowed());
		gen.writeEndObject();
		gen.writeEndObject();
	}
	
}