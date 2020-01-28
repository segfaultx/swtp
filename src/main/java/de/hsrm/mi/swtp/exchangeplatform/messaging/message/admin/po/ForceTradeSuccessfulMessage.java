package de.hsrm.mi.swtp.exchangeplatform.messaging.message.admin.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.Message;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.MessageType;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.serializer.admin.ForceTradeSuccessfulMessageSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;

import javax.jms.Topic;

@Value
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@JsonSerialize(using = ForceTradeSuccessfulMessageSerializer.class)
@RequiredArgsConstructor
@Builder
public class ForceTradeSuccessfulMessage extends Message {
	
	@JsonProperty(value = "type", required = true)
	@Schema(nullable = false, required = true, format = "string", type = "string")
	private final MessageType messageType = MessageType.TRADE_OFFER_SUCCESS;
	
	@JsonProperty(value = "message", defaultValue = "", required = true)
	@Schema(nullable = false, required = true, format = "string", type = "string")
	String message = "Trade durch Administrator war erfolgreich.";
	
	@JsonProperty(value = "new_timeslot", defaultValue = "null")
	@Schema(nullable = false, required = true)
	Timeslot newTimeslot;
	
	@JsonProperty(value = "old_timeslot_id", required = true)
	@Schema(nullable = false, required = true, format = "int64", type = "integer")
	Long oldTimeslotId;
	
	@JsonProperty(value = "topic")
	Topic topic;
	
}
