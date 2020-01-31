package de.hsrm.mi.swtp.exchangeplatform.event.admin.po;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class PORestrictionProcessorEventPublisher {
	
	ApplicationEventPublisher applicationEventPublisher;
	
	public void execute() {
		PORestrictionProcessorEvent event = new PORestrictionProcessorEvent(this);
		applicationEventPublisher.publishEvent(event);
	}
	
}
