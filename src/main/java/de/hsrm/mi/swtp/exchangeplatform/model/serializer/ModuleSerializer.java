package de.hsrm.mi.swtp.exchangeplatform.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;

import java.io.IOException;

public class ModuleSerializer extends StdSerializer<Module> {
	
	public ModuleSerializer() {
		this(null);
	}
	
	public ModuleSerializer(Class<Module> t) {
		super(t);
	}
	
	@Override
	public void serialize(Module value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		
		gen.writeStartObject();
		gen.writeNumberField("id", value.getId());
		gen.writeStringField("name", value.getName());
		gen.writeFieldName("timeslots");
		gen.writeStartArray();
		for(Timeslot timeslot : value.getTimeslots()) {
			gen.writeNumber(timeslot.getId());
		}
		gen.writeEndArray();
		gen.writeObjectField("po", value.getPo());
		gen.writeEndObject();
	}
	
}
