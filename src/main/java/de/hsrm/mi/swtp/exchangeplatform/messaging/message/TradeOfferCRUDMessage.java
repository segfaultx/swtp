package de.hsrm.mi.swtp.exchangeplatform.messaging.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import de.hsrm.mi.swtp.exchangeplatform.model.serializer.TradeOfferCRUDMessageSerializer;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonSerialize(using = TradeOfferCRUDMessageSerializer.class)
public class TradeOfferCRUDMessage extends Message {
	
	@JsonProperty("type")
	MessageType messageType;
	
	@JsonProperty(value = "message", defaultValue = "")
	String message = "Es gibt ein neues Tauschangebot.";
	
	@JsonProperty(value = "trade_offer", defaultValue = "{}")
	TradeOffer tradeOffer;
	
	@Builder
	public TradeOfferCRUDMessage(MessageType messageType, TradeOffer tradeOffer) {
		this.messageType = messageType;
		this.tradeOffer = tradeOffer;
	}
	
	public enum MessageType {
		TRADE_OFFER_CREATED,
		TRADE_OFFER_REMOVED,
		TRADE_OFFER_UPDATED,
	}
	
}
