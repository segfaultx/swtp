package de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager;

import com.google.common.reflect.TypeToken;
import java.lang.reflect.Type;

public abstract class AbstractDynamicTopicManager<T> implements DynamicTopicManager<T> {
	
	private final TypeToken<T> typeToken = new TypeToken<T>(getClass()) { };
	private final Type type = typeToken.getType();
	
	/**
	 * Creates a string which will be used as a Topic.name.
	 * Will use the ClassName of the specified Type <T> as part of the Topic-Name.
	 *
	 * @param id is the id of the object for which the Topic is created.
	 *
	 * @return a string which is formed out of {@link #TOPIC_NAME_BASE}.
	 */
	String createTopicName(final Long id) {
		return String.format(TOPIC_NAME_BASE, ((Class) type).getSimpleName(), id);
	}
}
