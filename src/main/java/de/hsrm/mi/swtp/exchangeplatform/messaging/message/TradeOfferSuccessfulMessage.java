package de.hsrm.mi.swtp.exchangeplatform.messaging.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.hsrm.mi.swtp.exchangeplatform.model.serializer.TradeOfferSuccessfulMessageSerializer;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Value
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonSerialize(using = TradeOfferSuccessfulMessageSerializer.class)
public class TradeOfferSuccessfulMessage implements Serializable {
	
	@JsonProperty("type")
	private final MessageType messageType = MessageType.TRADE_OFFER_SUCCESS;
	
	@JsonProperty(value = "message", defaultValue = "")
	String message;
	
	@JsonProperty(value = "value", defaultValue = "null")
	Long value;
	
	@Builder
	public TradeOfferSuccessfulMessage(Long value) {
		this.message = "Trade war erfolgreich.";
		this.value = value;
	}
}
