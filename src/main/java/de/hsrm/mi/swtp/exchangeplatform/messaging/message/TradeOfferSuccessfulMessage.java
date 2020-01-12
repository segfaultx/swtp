package de.hsrm.mi.swtp.exchangeplatform.messaging.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Value
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TradeOfferSuccessfulMessage {
	
	@JsonProperty("status")
	@Enumerated(EnumType.STRING)
	final TradeOfferStatusType status = TradeOfferStatusType.ACCEPTED;
	@JsonProperty("trade_offer_id")
	Long tradeOfferId;
	@JsonProperty("enter_timeslot")
	Long newTimeslotId;
	@JsonProperty("left_timeslot")
	Long leftTimeslotId;
	
	public TradeOfferSuccessfulMessage(Long tradeOfferId, Long newTimeslotId, Long leftTimeslotId) {
		this.tradeOfferId = tradeOfferId;
		this.newTimeslotId = newTimeslotId;
		this.leftTimeslotId = leftTimeslotId;
	}
}
