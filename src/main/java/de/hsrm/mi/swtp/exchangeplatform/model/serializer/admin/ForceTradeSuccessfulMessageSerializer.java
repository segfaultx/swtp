package de.hsrm.mi.swtp.exchangeplatform.model.serializer.admin;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.admin.po.ForceTradeSuccessfulMessage;

import java.io.IOException;

public class ForceTradeSuccessfulMessageSerializer extends StdSerializer<ForceTradeSuccessfulMessage> {
	
	public ForceTradeSuccessfulMessageSerializer() {
		this(null);
	}
	
	public ForceTradeSuccessfulMessageSerializer(Class<ForceTradeSuccessfulMessage> t) {
		super(t);
	}
	
	@Override
	public void serialize(ForceTradeSuccessfulMessage value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeStartObject();
		gen.writeStringField("type", value.getMessageType().name());
		gen.writeStringField("message", value.getMessage());
		
		gen.writeFieldName("value");
		gen.writeStartObject();
		gen.writeNumberField("new_timeslot_id", value.getNewTimeslot ().getId());
		gen.writeObjectField("new_timeslot", value.getNewTimeslot ());
		gen.writeNumberField("old_timeslot_id", value.getOldTimeslotId());
		gen.writeStringField("topic_name", value.getTopic().toString());
		gen.writeEndObject();
		
		gen.writeEndObject();
	}
	
}
