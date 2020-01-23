package de.hsrm.mi.swtp.exchangeplatform.messaging.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;

@Value
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TradeOfferSuccessfulMessage {
	
	@JsonProperty("type")
	MessageType messageType;
	
	@JsonProperty(value = "message", defaultValue = "")
	String message;
	
	@JsonProperty(value = "value", defaultValue = "null")
	Long value;
	
	@Builder
	public TradeOfferSuccessfulMessage(Long value) {
		this.messageType = MessageType.TRADE_OFFER_SUCCESS;
		this.message = "Trade war erfolgreich.";
		this.value = value;
	}
}
