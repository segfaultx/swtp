package de.hsrm.mi.swtp.exchangeplatform.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.io.IOException;
import java.time.LocalTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocalTimeSerializer extends StdSerializer<LocalTime> {
	
	final ObjectMapper mapper = new ObjectMapper();
	
	protected LocalTimeSerializer(Class<LocalTime> t) {
		super(t);
	}
	
	public LocalTimeSerializer() {
		this(null);
	}
	
	@Override
	public void serialize(LocalTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeRawValue(mapper.writeValueAsString(value));
	}
}
