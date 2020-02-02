package de.hsrm.mi.swtp.exchangeplatform.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.TradeOfferCRUDMessage;

import java.io.IOException;

public class TradeOfferCRUDMessageSerializer extends StdSerializer<TradeOfferCRUDMessage> {
	
	public TradeOfferCRUDMessageSerializer() {
		this(null);
	}
	
	public TradeOfferCRUDMessageSerializer(Class<TradeOfferCRUDMessage> t) {
		super(t);
	}
	
	@Override
	public void serialize(TradeOfferCRUDMessage value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeStartObject();
		gen.writeStringField("type", value.getMessageType().toString());
		gen.writeStringField("message", value.getMessage());
		gen.writeObjectField("value", value.getTradeOffer());
		gen.writeEndObject();
	}
	
}
