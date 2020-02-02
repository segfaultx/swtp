package de.hsrm.mi.swtp.exchangeplatform.messaging.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.hsrm.mi.swtp.exchangeplatform.model.serializer.ExchangeplatformMessageSerializer;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonSerialize(using = ExchangeplatformMessageSerializer.class)
public class ExchangeplatformStatusMessage extends Message {
	
	@JsonProperty("type")
	MessageType messageType = MessageType.EXCHANGEPLATFORM_STATUS;
	
	@JsonProperty("tradesActive")
	Boolean isActive = false;
	
	@JsonProperty("message")
	String message = "Tauschbörse ist jetzt %s.";
	
	@Builder
	public ExchangeplatformStatusMessage(Boolean isActive) {
		this.isActive = isActive;
	}
	
	public String getMessage() {
		return String.format(message, isActive ? "aktiv": "inaktiv");
	}
	
}
