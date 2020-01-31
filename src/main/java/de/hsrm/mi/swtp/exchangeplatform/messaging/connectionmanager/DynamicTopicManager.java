package de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager;

import javax.jms.Topic;
import javax.jms.TopicSession;

/**
 * An interface for classes which manage {@link Topic Topics} created at Runtime.
 * Provides those managers with a {@link #TOPIC_NAME_BASE naming string template} for creating Topic names and a
 * method for creating a Topic for a given object of the specified generic type <T>.
 */
public interface DynamicTopicManager<T> {
	
	/**
	 * first %s: type of data which is transferred
	 * second %s: id of instance
	 */
	String TOPIC_NAME_BASE = "%s-%s";
	
	/**
	 * Creates a simple {@link Topic} for a given object/{@link de.hsrm.mi.swtp.exchangeplatform.model.data.Model}.
	 *
	 * @param obj the object for which the {@link Topic} is created.
	 *
	 * @return a simple {@link Topic}.
	 */
	Topic createTopic(final T obj);
	
	Topic getTopic(Long id);
	
	Topic getTopic(T obj);
	
	TopicSession getSession(Long id);
	
	TopicSession getSession(T obj);
	
}
