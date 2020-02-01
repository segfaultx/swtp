package de.hsrm.mi.swtp.exchangeplatform.messaging.message.admin.po.violation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.MessageType;
import de.hsrm.mi.swtp.exchangeplatform.model.serializer.admin.po.CPViolationMessageSerializer;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@JsonSerialize(using = CPViolationMessageSerializer.class)
public class CPViolationMessage extends POViolationMessage {
	
	@JsonProperty("type")
	MessageType messageType = MessageType.PO_VIOLATION_CP;
	
	@JsonProperty("message")
	String message = "Du hast mit deiner aktuellen Belegung zu viele CP.";
	
	@JsonProperty("max_cp")
	Long maxCPByPO;
	
	@JsonProperty("occupied_cp")
	Long userCP;
	
	@Builder
	public CPViolationMessage(Long maxCPByPO, Long userCP) {
		this.maxCPByPO = maxCPByPO;
		this.userCP = userCP;
	}
}
