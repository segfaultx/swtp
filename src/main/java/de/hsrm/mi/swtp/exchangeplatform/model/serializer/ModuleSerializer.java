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
		gen.writeNumberField("module_number", value.getModuleNumber());
		gen.writeStringField("name", value.getName());
		gen.writeStringField("contraction", value.getName());
		gen.writeNumberField("credit_points", value.getCreditPoints());
		gen.writeNumberField("semester", value.getSemester());
		gen.writeObjectField("lecturer", value.getLecturer());
		
		if(value.getTimeslots() != null){
			gen.writeFieldName("timeslots");
			gen.writeStartArray();
			for(Timeslot t: value.getTimeslots()) {
				Module m = t.getModule();
				m.setTimeslots(null);
				Timeslot t_m = t;
				t_m.setModule(m);
				gen.writeObject(t_m);
			}
			gen.writeEndArray();
		}
		
		gen.writeObjectField("po", value.getPo());
		gen.writeEndObject();
	}
	
}
