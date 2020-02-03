package de.hsrm.mi.swtp.exchangeplatform.configuration;

import de.hsrm.mi.swtp.exchangeplatform.event.admin.po.PORestrictionProcessorEvent;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppConfig {
	
	/**
	 * Allows asynchronous event-bus actions and handling.
	 * Atm it is solely used for the {@link PORestrictionProcessorEvent} and its corresponding Publisher and Listener.
	 * Without this {@link ApplicationEventMulticaster} the event handling would <b>blocking</b>!
	 *
	 * @see <a href="https://www.baeldung.com/spring-events">Baeldung</a>
	 *
	 * @return a {@link SimpleApplicationEventMulticaster}
	 */
	@Bean(name = "applicationEventMulticaster")
	public ApplicationEventMulticaster simpleApplicationEventMulticaster() {
		SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();
		eventMulticaster.setTaskExecutor(new SimpleAsyncTaskExecutor());
		return eventMulticaster;
	}
	
}
