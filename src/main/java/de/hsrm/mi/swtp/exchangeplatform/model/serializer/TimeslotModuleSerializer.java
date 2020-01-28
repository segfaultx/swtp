package de.hsrm.mi.swtp.exchangeplatform.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;

import java.io.IOException;

/**
 * A custom serializer for serialzing a {@link Module} within a {@link Timeslot}.
 * This serializer differs from the {@link ModuleSerializer} in the sense that this serialzer represents a {@link Module modules} timeslots by their ids
 * instead of using the full object - which is to prevent a loop via Timeslot -> Modules -> Timeslots -> Module ...
 */
public class TimeslotModuleSerializer extends StdSerializer<Module> {
	
	public TimeslotModuleSerializer() {
		this(null);
	}
	
	public TimeslotModuleSerializer(Class<Module> t) {
		super(t);
	}
	
	@Override
	public void serialize(Module value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeStartObject();
		gen.writeNumberField("id", value.getId());
		gen.writeNumberField("module_number", value.getModuleNumber());
		gen.writeStringField("name", value.getName());
		gen.writeStringField("contraction", value.getContraction());
		gen.writeNumberField("credit_points", value.getCreditPoints());
		gen.writeNumberField("semester", value.getSemester());
		gen.writeObjectField("lecturer", value.getLecturer());
		
		if(value.getTimeslots() != null){
			gen.writeFieldName("timeslots");
			gen.writeStartArray();
			for(Timeslot t: value.getTimeslots()) gen.writeNumber(t.getId());
			gen.writeEndArray();
		}
		
		gen.writeObjectField("po", value.getPo());
		gen.writeEndObject();
	}
	
}
