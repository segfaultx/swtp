package de.hsrm.mi.swtp.exchangeplatform.messaging.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Builder
public class ProgressiveRegulationViolationMessage {
	
	@JsonProperty("type")
	MessageType messageType = MessageType.PO_VIOLATION_PROGRESSIVE_REGULATION;
	
	@JsonProperty("message")
	String message = "Du versößt gegen die PO, mit deiner aktuellen Belegung.";
	
	@JsonProperty("modules_not_allowed")
	List<Long> modulesNotAllowed;
	
}
