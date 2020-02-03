package de.hsrm.mi.swtp.exchangeplatform.service.admin.po.filter;

import de.hsrm.mi.swtp.exchangeplatform.event.admin.po.PORestrictionProcessorEvent;
import de.hsrm.mi.swtp.exchangeplatform.event.admin.po.PORestrictionProcessorEventListener;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

/**
 * A wrapper which contains an instance of {@link PORestrictionProcessor} and a {@link TaskExecutor}}.
 * This executor is used to run the {@link PORestrictionProcessor#startProcessing()} method in a seperate {@link Thread}.
 * <p>
 * This executor will be started via {@link de.hsrm.mi.swtp.exchangeplatform.event.admin.po.PORestrictionProcessorEventListener#onApplicationEvent(PORestrictionProcessorEvent)}.
 *
 * @see PORestrictionProcessorEventListener
 */
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PORestrictionProcessorExecutor {
	
	TaskExecutor taskExecutor;
	PORestrictionProcessor poRestrictionProcessor;
	
	public void execute() {
		taskExecutor.execute(poRestrictionProcessor);
	}
	
}
