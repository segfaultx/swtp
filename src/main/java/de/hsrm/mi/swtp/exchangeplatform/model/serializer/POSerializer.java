package de.hsrm.mi.swtp.exchangeplatform.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
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
		gen.writeStringField("major", value.getMajor());
		gen.writeNumberField("valid_since_year", value.getValidSinceYear());
		gen.writeFieldName("modules");
		gen.writeStartArray();
		for(Module module : value.getModules()) {
			gen.writeNumber(module.getId());
		}
		gen.writeEndArray();
		gen.writeEndObject();
	}
}
