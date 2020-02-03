package de.hsrm.mi.swtp.exchangeplatform.event.admin.po;

import de.hsrm.mi.swtp.exchangeplatform.service.admin.po.filter.PORestrictionProcessorExecutor;
import de.hsrm.mi.swtp.exchangeplatform.utils.LoggingFormat;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PORestrictionProcessorEventListener implements ApplicationListener<PORestrictionProcessorEvent> {
	
	PORestrictionProcessorExecutor poRestrictionProcessorExecutor;
	
	@Override
	public void onApplicationEvent(PORestrictionProcessorEvent event) {
		LoggingFormat.infoBoxed("Executing PORestrictionProcessorExecutor...");
		poRestrictionProcessorExecutor.execute();
		LoggingFormat.infoBoxed("FINISHED PORestrictionProcessorExecutor...");
	}
}