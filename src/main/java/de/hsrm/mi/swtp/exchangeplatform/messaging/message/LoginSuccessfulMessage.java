package de.hsrm.mi.swtp.exchangeplatform.messaging.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.hsrm.mi.swtp.exchangeplatform.model.serializer.LoginSuccessfulMessageSerializer;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@JsonSerialize(using = LoginSuccessfulMessageSerializer.class)
public class LoginSuccessfulMessage implements Serializable {
	
	@JsonProperty("type")
	MessageType messageType = MessageType.LOGIN;
	
	@JsonProperty(value = "message", defaultValue = "")
	String message;
	
	@JsonProperty(value = "value", defaultValue = "{}")
	Map<String, String> value;
	
	@Builder
	public LoginSuccessfulMessage() {
		this.value = new HashMap<>();
		this.value.put("timestamp", LocalDateTime.now().toString());
		this.message = String.format("Login erfolgreich, um %s.", this.getTimestampString());
	}
	
	public String getTimestampString(){
		return this.value.get("timestamp");
	}
	
}
