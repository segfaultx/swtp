package de.hsrm.mi.swtp.exchangeplatform.messaging.message;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Value
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TradeOfferSuccessfulMessage {
	
	@Enumerated(EnumType.STRING)
	final TradeOfferStatusType status = TradeOfferStatusType.ACCEPTED;
	Long tradeOfferId;
	
	public TradeOfferSuccessfulMessage(Long tradeOfferId) {
		this.tradeOfferId = tradeOfferId;
	}
}
