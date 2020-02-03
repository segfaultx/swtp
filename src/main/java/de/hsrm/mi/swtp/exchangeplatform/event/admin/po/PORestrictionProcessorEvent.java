package de.hsrm.mi.swtp.exchangeplatform.event.admin.po;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEvent;

/**
 * An {@link ApplicationEvent} which is used when a {@link de.hsrm.mi.swtp.exchangeplatform.model.data.PORestriction} has been changed and the exchangeplatform has been {@link de.hsrm.mi.swtp.exchangeplatform.service.rest.POService#update re-activated}.
 * <b>This event may only be called by an {@link de.hsrm.mi.swtp.exchangeplatform.model.data.enums.Roles#ADMIN}.</b>
 */
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