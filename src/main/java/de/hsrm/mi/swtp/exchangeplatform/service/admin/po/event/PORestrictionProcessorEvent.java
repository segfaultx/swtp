package de.hsrm.mi.swtp.exchangeplatform.service.admin.po.event;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEvent;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PORestrictionProcessorEvent extends ApplicationEvent {
	
	/**
	 * Create a new {@code ApplicationEvent}.
	 *
	 * @param source the object on which the event initially occurred or with which the event is
	 *               associated (never {@code null})
	 */
	public PORestrictionProcessorEvent(Object source) {
		super(source);
	}
}