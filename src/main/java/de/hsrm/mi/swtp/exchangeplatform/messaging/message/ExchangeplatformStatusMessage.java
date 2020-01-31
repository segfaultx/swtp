package de.hsrm.mi.swtp.exchangeplatform.messaging.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.hsrm.mi.swtp.exchangeplatform.model.serializer.ExchangeplatformMessageSerializer;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonSerialize(using = ExchangeplatformMessageSerializer.class)
public class ExchangeplatformStatusMessage extends Message {
	
	@JsonProperty("type")
	MessageType messageType = MessageType.EXCHANGEPLATFORM_STATUS;
	
	@JsonProperty("tradesActive")
	Boolean isActive = false;
	
	@JsonProperty("message")
	String message;
	
	public ExchangeplatformStatusMessage(Boolean isActive) {
		this(isActive, "Tauschbörse ist jetzt " + (isActive ? "aktiv.": "inaktiv."));
	}
	
	public ExchangeplatformStatusMessage(Boolean isActive, String message) {
		this.isActive = isActive;
		this.message = message;
	}
	
}
