package de.hsrm.mi.swtp.exchangeplatform.service.admin.po.filter;

import de.hsrm.mi.swtp.exchangeplatform.messaging.message.admin.po.violation.POViolationMessage;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.RestrictionType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PORestrictionFilterResult {
	
	User student;
	Map<RestrictionType, POViolationMessage> messages;
	
	@Builder
	public PORestrictionFilterResult(User student) {
		this.student = student;
		this.messages = new HashMap<>();
	}
	
	public PORestrictionFilterResult extend(RestrictionType type, POViolationMessage message) {
		this.messages.put(type, message);
		return this;
	}
	
	public PORestrictionFilterResult extend(RestrictionType type, PORestrictionFilterResult result) {
		this.messages.put(type, result.getMessages().get(type));
		return this;
	}
	
}
