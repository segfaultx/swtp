package de.hsrm.mi.swtp.exchangeplatform.messaging.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public abstract class Message implements Serializable {
	
	ObjectMapper objectMapper = new ObjectMapper();
	
	@JsonProperty(value = "type", required = true)
	@Schema(nullable = false, required = true, format = "string", type = "string")
	private final MessageType messageType = MessageType.TRADE_OFFER_SUCCESS;
	
	@JsonProperty(value = "message", defaultValue = "", required = true)
	@Schema(nullable = false, required = true, format = "string", type = "string")
	String message = "Trade durch Administrator war erfolgreich.";
	
	@Override
	public String toString() {
		return "Message{" + "messageType=" + messageType + ", message='" + message + '\'' + '}';
	}
	
	public String toJSON() throws JsonProcessingException {
		return objectMapper.writeValueAsString(this);
	}
	
}
