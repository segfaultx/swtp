package de.hsrm.mi.swtp.exchangeplatform.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.TradeOfferSuccessfulMessage;

import java.io.IOException;

public class TradeOfferSuccessfulMessageSerializer extends StdSerializer<TradeOfferSuccessfulMessage> {
	
	public TradeOfferSuccessfulMessageSerializer() {
		this(null);
	}
	
	public TradeOfferSuccessfulMessageSerializer(Class<TradeOfferSuccessfulMessage> t) {
		super(t);
	}
	
	@Override
	public void serialize(TradeOfferSuccessfulMessage value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeStartObject();
		gen.writeStringField("type", value.getMessageType().name());
		gen.writeStringField("message", value.getMessage());
		gen.writeNumberField("value", value.getValue());
		gen.writeEndObject();
	}
	
}
