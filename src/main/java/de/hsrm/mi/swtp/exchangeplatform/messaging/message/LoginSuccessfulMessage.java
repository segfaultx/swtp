package de.hsrm.mi.swtp.exchangeplatform.messaging.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.hsrm.mi.swtp.exchangeplatform.model.serializer.ExchangeplatformMessageSerializer;
import de.hsrm.mi.swtp.exchangeplatform.model.serializer.LoginSuccessfulMessageSerializer;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@JsonSerialize(using = LoginSuccessfulMessageSerializer.class)
public class LoginSuccessfulMessage implements Serializable {

	@JsonProperty("message")
	String message;
	
	@Builder
	public LoginSuccessfulMessage() {
		this.message = String.format("Log in successful at %s.", LocalDateTime.now().toString());
	}
	
}
