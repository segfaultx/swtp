package de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager;

import javax.jms.Topic;

public interface DynamicTopicManager<T> {
	
	/**
	 * first %s: type of data which is transferred
	 * second %s: id of instance
	 */
	String TOPIC_NAME_BASE = "exchangeplatform:%s-%s";
	
	Topic createTopic(final T obj);
	
}
