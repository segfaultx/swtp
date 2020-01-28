package de.hsrm.mi.swtp.exchangeplatform.messaging.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

// TODO: will make use of it.
public abstract class Message implements Serializable {
	
	@JsonProperty(value = "type", required = true)
	@Schema(nullable = false, required = true, format = "string", type = "string")
	private final MessageType messageType = MessageType.TRADE_OFFER_SUCCESS;
	
	@JsonProperty(value = "message", defaultValue = "", required = true)
	@Schema(nullable = false, required = true, format = "string", type = "string")
	String message = "Trade durch Administrator war erfolgreich.";
	
}
