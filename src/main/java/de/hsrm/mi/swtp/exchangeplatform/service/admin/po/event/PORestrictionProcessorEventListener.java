package de.hsrm.mi.swtp.exchangeplatform.service.admin.po.event;

import de.hsrm.mi.swtp.exchangeplatform.service.admin.po.filter.PORestrictionProcessorExecutor;
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
		final String str = "Executing PORestrictionProcessorExecutor...";
		String vert = "─".repeat(str.length() + 4);
		log.info(String.format("\n┌%s┐\n│  %s  │\n└%s┘\n", vert, str, vert));
		poRestrictionProcessorExecutor.execute();
	}
}