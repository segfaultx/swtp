package de.hsrm.mi.swtp.exchangeplatform.messaging.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.enums.MessageType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

/**
 * An abstract class for messages which may be sent via JMS.
 * Contains the basic structure of a message. Each message must contain the following (JSON key in brackets):
 * - {@link #messageType} ("type"): will be used as an identifier when de-serializing the JSON in the client and is of type {@link MessageType},
 * - {@link #message} ("message"): is a string which is optional and can be used as a human friendly message,
 * - and a value ("value") which may be of any type and each message class may define the format on its own.
 */
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
@Schema(description = "An abstract class for messages which may be sent via JMS.")
public abstract class Message implements Serializable {
	
	ObjectMapper objectMapper = new ObjectMapper();
	
	@JsonProperty(value = "type", required = true)
	@Schema(nullable = false,
			description = "A signifier which is used to identify the kind of value which is transmitted during de-serialization.",
			example = "LOGIN",
			required = true,
			format = "string",
			type = "string")
	private final MessageType messageType = MessageType.LOGIN;

	@JsonProperty(value = "message", defaultValue = "", required = true)
	@Schema(nullable = false,
			description = "A human friendly message which sums up the main content of the message.",
			example = "You signed off successfully.",
			required = true,
			format = "string",
			type = "string")
	String message = "";
	
	@Override
	public String toString() {
		return "Message{" + "messageType=" + messageType + ", message='" + message + '\'' + '}';
	}
	
	/**
	 * @return a JSON representation of the message.
	 */
	public String toJSON() throws JsonProcessingException {
		return objectMapper.writeValueAsString(this);
	}
	
}
