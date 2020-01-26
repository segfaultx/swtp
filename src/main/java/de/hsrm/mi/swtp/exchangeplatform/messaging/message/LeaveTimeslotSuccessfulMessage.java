package de.hsrm.mi.swtp.exchangeplatform.messaging.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.serializer.LeaveModuleSuccessfulMessageSerializer;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalTime;

@Data
@JsonSerialize(using = LeaveModuleSuccessfulMessageSerializer.class)
public class LeaveTimeslotSuccessfulMessage implements Serializable {
	
	@JsonProperty("type")
	MessageType messageType = MessageType.LEAVE_TIMESLOT_SUCCESS;
	
	@JsonProperty(value = "message", defaultValue = "")
	String message = "Austritt aus Timeslot %swar erfolgreich.";
	
	@JsonProperty(value = "timestamp", defaultValue = "{}")
	LocalTime time;
	
	@JsonProperty(value = "timeslot", defaultValue = "{}")
	Timeslot timeslot;
	
	@Builder
	public LeaveTimeslotSuccessfulMessage(LocalTime time, Timeslot timeslot) {
		this.time = time;
		this.timeslot = timeslot;
	}
	
	public String getMessage() {
		return String.format(message, timeslot == null ? "" : "\"" + timeslot.getId() + "\" ");
	}
}
