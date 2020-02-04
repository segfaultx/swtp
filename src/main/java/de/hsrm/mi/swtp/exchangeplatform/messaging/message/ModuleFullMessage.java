package de.hsrm.mi.swtp.exchangeplatform.messaging.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.enums.MessageType;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import de.hsrm.mi.swtp.exchangeplatform.model.serializer.ModuleFullMessageSerializer;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonSerialize(using = ModuleFullMessageSerializer.class)
public class ModuleFullMessage extends Message {
	
	@JsonProperty("type")
	MessageType messageType = MessageType.ALL_MODULE_TIMESLOTS_FULL;
	
	@JsonProperty(value = "message", defaultValue = "")
	String message = "";
	
	@JsonProperty(value = "module", defaultValue = "{}")
	Module module;
	
	@Builder
	public ModuleFullMessage(Module module) {
		this.module = module;
	}
}
