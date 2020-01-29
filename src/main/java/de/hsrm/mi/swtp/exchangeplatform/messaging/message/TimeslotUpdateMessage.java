package de.hsrm.mi.swtp.exchangeplatform.messaging.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.serializer.TimeslotUpdateMessageSerializer;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonSerialize(using = TimeslotUpdateMessageSerializer.class)
public class TimeslotUpdateMessage implements Serializable {
	
	@JsonProperty("type")
	MessageType messageType = MessageType.UPDATE_TIMESLOT;
	
	@JsonProperty(value = "message", defaultValue = "")
	String message = "";
	
	@JsonProperty(value = "timeslot", defaultValue = "{}")
	Timeslot timeslot;
	
	@Builder
	public TimeslotUpdateMessage(Timeslot timeslot) {
		this.timeslot = timeslot;
	}
}
