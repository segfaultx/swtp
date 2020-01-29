package de.hsrm.mi.swtp.exchangeplatform.messaging.message.admin.po.violation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.MessageType;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.DayOfWeek;
import de.hsrm.mi.swtp.exchangeplatform.model.serializer.admin.po.DualPOViolationMessageSerializer;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@JsonSerialize(using = DualPOViolationMessageSerializer.class)
public class DualPOViolationMessage {
	
	@JsonProperty("type")
	MessageType messageType = MessageType.PO_VIOLATION_DUAL;
	
	@JsonProperty("message")
	String message = "Du hast an einem gesperrten Tag Veranstaltungen belegt.";
	
	@JsonProperty("blocked_day")
	DayOfWeek blockedDay;
	
	@JsonProperty("timeslots_not_allowed")
	List<Long> timeslotsNotAllowed;
	
	@Builder
	public DualPOViolationMessage(DayOfWeek blockedDay, List<Long> timeslotsNotAllowed) {
		this.blockedDay = blockedDay;
		this.timeslotsNotAllowed = timeslotsNotAllowed;
	}
}
