package de.hsrm.mi.swtp.exchangeplatform.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.hsrm.mi.swtp.exchangeplatform.messaging.ExchangeplatformStatusMessage;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;

import java.io.IOException;

public class ExchangeplatformMessageSerializer extends StdSerializer<ExchangeplatformStatusMessage> {
	
	public ExchangeplatformMessageSerializer() {
		this(null);
	}
	
	public ExchangeplatformMessageSerializer(Class<ExchangeplatformStatusMessage> t) {
		super(t);
	}
	
	@Override
	public void serialize(ExchangeplatformStatusMessage value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeStartObject();
		gen.writeStringField("message", value.getMessage());
		gen.writeStringField("status", value.getStatus().toString());
		gen.writeEndObject();
	}

}
