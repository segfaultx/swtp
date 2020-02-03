package de.hsrm.mi.swtp.exchangeplatform.service.admin.po.filter;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

/**
 * A wrapper which contains an instance of {@link PORestrictionProcessor} and a {@link TaskExecutor}.
 * This executor is used to run a the {@link PORestrictionProcessor#startProcessing()} method.
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
