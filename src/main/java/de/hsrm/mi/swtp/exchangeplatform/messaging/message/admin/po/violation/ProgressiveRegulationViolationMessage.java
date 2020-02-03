package de.hsrm.mi.swtp.exchangeplatform.messaging.message.admin.po.violation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.MessageType;
import de.hsrm.mi.swtp.exchangeplatform.model.serializer.admin.po.ProgressiveRegulationViolationMessageSerializer;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@JsonSerialize(using = ProgressiveRegulationViolationMessageSerializer.class)
public class ProgressiveRegulationViolationMessage extends POViolationMessage {
	
	@JsonProperty("type")
	MessageType messageType = MessageType.PO_VIOLATION_PROGRESSIVE_REGULATION;
	
	@JsonProperty("message")
	String message = "Du versößt gegen die Fortschrittsregelung, mit deiner aktuellen Belegung.";
	
	@JsonProperty("modules_not_allowed")
	List<Long> modulesNotAllowed;
	
	@Builder
	public ProgressiveRegulationViolationMessage(List<Long> modulesNotAllowed) {
		this.modulesNotAllowed = modulesNotAllowed;
	}
}
