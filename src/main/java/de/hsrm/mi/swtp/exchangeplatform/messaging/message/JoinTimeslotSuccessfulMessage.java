package de.hsrm.mi.swtp.exchangeplatform.messaging.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.enums.MessageType;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.serializer.JoinTimeslotSuccessfulMessageSerializer;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalTime;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonSerialize(using = JoinTimeslotSuccessfulMessageSerializer.class)
public class JoinTimeslotSuccessfulMessage extends Message {
	
	@JsonProperty("type")
	MessageType messageType = MessageType.JOIN_TIMESLOT_SUCCESS;
	
	@JsonProperty(value = "message", defaultValue = "Eintritt in Timeslot war erfolgreich.")
	String message = "Eintritt in Timeslot %swar erfolgreich.";
	
	@JsonProperty(value = "timestamp", defaultValue = "{}")
	LocalTime time;
	
	@JsonProperty(value = "timeslot", defaultValue = "{}")
	Timeslot timeslot;
	
	@Builder
	public JoinTimeslotSuccessfulMessage(LocalTime time, Timeslot timeslot) {
		this.time = time;
		this.timeslot = timeslot;
	}
	
	public String getMessage() {
		return String.format(message, timeslot == null ? "" : "\"" + timeslot.getId() + "\" ");
	}
}
