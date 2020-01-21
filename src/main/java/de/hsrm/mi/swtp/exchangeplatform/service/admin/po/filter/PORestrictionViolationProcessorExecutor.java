package de.hsrm.mi.swtp.exchangeplatform.service.admin.po.filter;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

/**
 * A wrapper which contains an instance of {@link PORestrictionViolationProcessor} and a {@link TaskExecutor}.
 * This executor is used to run a the {@link PORestrictionViolationProcessor#startProcessing()} method in a separate, non-blocking Thread.
 */
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class PORestrictionViolationProcessorExecutor {
	
	TaskExecutor taskExecutor;
	PORestrictionViolationProcessor poRestrictionViolationProcessor;
	
	public void execute() {
		taskExecutor.execute(poRestrictionViolationProcessor);
	}
	
}
