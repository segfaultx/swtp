package de.hsrm.mi.swtp.exchangeplatform.messaging.message.admin.po.violation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.enums.MessageType;
import de.hsrm.mi.swtp.exchangeplatform.model.serializer.admin.po.SemesterViolationMessageSerializer;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@JsonSerialize(using = SemesterViolationMessageSerializer.class)
public class SemesterViolationMessage extends POViolationMessage {
	
	@JsonProperty("type")
	MessageType messageType = MessageType.PO_VIOLATION_SEMESTER;
	
	@JsonProperty("message")
	String message = "Du versößt gegen die PO, mit deiner aktuellen Belegung.";
	
	@JsonProperty("min_semester")
	Long poSemester;
	
	@JsonProperty("modules_not_allowed")
	List<Long> modulesNotAllowed;
	
	@JsonProperty("modules_missing")
	List<Long> missingModules;
	
	@Builder
	public SemesterViolationMessage(List<Long> modulesNotAllowed, List<Long> missingModules, Long poSemester) {
		this.modulesNotAllowed = modulesNotAllowed;
		this.missingModules = missingModules;
		this.poSemester = poSemester;
	}
}
