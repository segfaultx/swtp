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
	
	/**
	 * Is the {@link User} which is affected by PO-Restriction changes.
	 */
	User student;
	/**
	 * A map which contains all {@link POViolationMessage messages} of the {@link #student} which is affected by the PO changes.
	 */
	Map<RestrictionType, POViolationMessage> messages;
	
	@Builder
	public PORestrictionFilterResult(User student) {
		this.student = student;
		this.messages = new HashMap<>();
	}
	
	/**
	 * Adds {@link POViolationMessage violation messages} to the {@link PORestrictionFilterResult}.
	 *
	 * @param type    defines the {@link RestrictionType type} of the new violation message.
	 * @param message is the new message which contains the {@link POViolationMessage} which will be sent to a {@link User} as a notification.
	 *
	 * @return the extended {@link PORestrictionFilterResult} to which the given message is added unter the key {@link RestrictionType}.
	 */
	public PORestrictionFilterResult extend(RestrictionType type, POViolationMessage message) {
		this.messages.put(type, message);
		return this;
	}
	
	/**
	 * @see #extend(RestrictionType, POViolationMessage)
	 */
	public PORestrictionFilterResult extend(RestrictionType type, PORestrictionFilterResult result) {
		this.messages.put(type, result.getMessages().get(type));
		return this;
	}
	
}
