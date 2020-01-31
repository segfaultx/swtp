package de.hsrm.mi.swtp.exchangeplatform.messaging.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import de.hsrm.mi.swtp.exchangeplatform.model.serializer.LeaveModuleSuccessfulMessageSerializer;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalTime;

@Data
@JsonSerialize(using = LeaveModuleSuccessfulMessageSerializer.class)
public class LeaveModuleSuccessfulMessage extends Message {
	
	@JsonProperty("type")
	MessageType messageType = MessageType.LEAVE_MODULE_SUCCESS;
	
	@JsonProperty(value = "message", defaultValue = "")
	String message = "Austritt aus Modul %swar erfolgreich.";
	
	@JsonProperty(value = "timestamp", defaultValue = "{}")
	LocalTime time;
	
	@JsonProperty(value = "module", defaultValue = "{}")
	Module module;
	
	@Builder
	public LeaveModuleSuccessfulMessage(LocalTime time, Module module) {
		this.time = time;
		this.module = module;
	}
	
	public String getMessage() {
		return String.format(message, module == null ? "" : "\"" + module.getName() + "\" ");
	}
}
