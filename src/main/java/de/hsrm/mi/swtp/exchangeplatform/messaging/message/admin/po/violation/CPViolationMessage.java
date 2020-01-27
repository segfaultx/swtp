package de.hsrm.mi.swtp.exchangeplatform.messaging.message.admin.po.violation;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.MessageType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Builder
public class CPViolationMessage {
	
	@JsonProperty("type")
	MessageType messageType = MessageType.PO_VIOLATION_CP;
	
	@JsonProperty("message")
	String message = "Du hast mit deiner aktuellen Belegung zu viele CP.";
	
	@JsonProperty("max_cp")
	Long maxCPByPO;
	
	@JsonProperty("occupied_cp")
	Long userCP;
	
}
