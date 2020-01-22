package de.hsrm.mi.swtp.exchangeplatform.messaging.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
//@JsonSerialize(using = LoginSuccessfulMessageSerializer.class)
public class LoginSuccessfulMessage implements Serializable {
	
	@JsonProperty("type")
	MessageType messageType;
	
	@JsonProperty(value = "message", defaultValue = "")
	String message;
	
	@JsonProperty(value = "value", defaultValue = "null")
	String value;
	
	@Builder
	public LoginSuccessfulMessage() {
		this.value = LocalDateTime.now().toString();
		this.message = String.format("Login erfolgreich, um %s.", this.value);
	}
	
}
