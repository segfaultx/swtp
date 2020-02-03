package de.hsrm.mi.swtp.exchangeplatform.model.serializer.admin.po;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.hsrm.mi.swtp.exchangeplatform.model.data.PO;

import java.io.IOException;

public class POSerializer extends StdSerializer<PO> {
	
	public POSerializer() {
		this(null);
	}
	
	public POSerializer(Class<PO> t) {
		super(t);
	}

	@Override
	public void serialize(PO value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeStartObject();
		gen.writeNumberField("id", value.getId());
		gen.writeStringField("title", value.getTitle());
		gen.writeStringField("valid_since", value.getValidSince().toString());
		gen.writeNumberField("semester_count", value.getSemesterCount());
		gen.writeStringField("date_start", String.valueOf(value.getDateStart()));
		gen.writeStringField("date_end", String.valueOf(value.getDateEnd()));
		gen.writeStringField("major", value.getMajor());
		gen.writeObjectField("restriction", value.getRestriction());
		gen.writeEndObject();
	}
}
