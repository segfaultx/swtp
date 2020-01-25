package de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager;

import com.google.common.reflect.TypeToken;
import de.hsrm.mi.swtp.exchangeplatform.messaging.factory.TopicFactory;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Model;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicSession;
import java.lang.reflect.Type;
import java.util.HashMap;

@Slf4j
public abstract class AbstractDynamicTopicManager<T extends Model> implements DynamicTopicManager<T> {
	
	private final TypeToken<T> typeToken = new TypeToken<T>(getClass()) {};
	private final Type type = typeToken.getType();
	
	/**
	 * A collection which saves all dynamically created Topics and their TopicSessions of {@link T} and maps their
	 * {@link T#getId()} to the corresponding{@link Topic}.
	 * <p>
	 * <Long, TopicCreationDTO> := Long -> {@link T#getId()}, TopicCreationDTO -> contains the TopicSession and Topic
	 *
	 * @see TopicCreationDTO
	 */
	HashMap<Long, TopicCreationDTO> topicSessionMap;
	@Autowired
	TopicFactory topicFactory;
	
	public AbstractDynamicTopicManager() {
		this.topicSessionMap = new HashMap<>();
	}
	
	/**
	 * Creates a string which will be used as a Topic.name.
	 * Will use the ClassName of the specified Type <T> as part of the Topic-Name.
	 *
	 * @param id is the id of the object for which the Topic is created.
	 *
	 * @return a string which is formed out of {@link #TOPIC_NAME_BASE}.
	 */
	String createTopicName(final Long id) {
		if(id == null) return null;
		return String.format(TOPIC_NAME_BASE, ((Class) type).getSimpleName(), id);
	}
	
	/**
	 * A simple helper method for creating a new TopicSession and a new (ActiveMQ-)Topic.
	 *
	 * @param id         {@link #createTopicName(Long)}
	 * @param connection is the {@link TopicConnection} used to create new {@link TopicSession sessions} and {@link ActiveMQTopic topics} within.
	 *
	 * @return {@link TopicCreationDTO}
	 */
	TopicCreationDTO createTopic(final Long id, TopicConnection connection) throws JMSException {
		final String topicName = this.createTopicName(id);
		final TopicCreationDTO dto = topicFactory.createTopic(connection, topicName);
		
		log.info(String.format(" + created topic name: %s", topicName));
		log.info(String.format(" + created topic: %s", dto.getTopic().toString()));
		
		return this.topicSessionMap.put(id, dto);
	}
	
	@Override
	public Topic getTopic(Long id) {
		return this.topicSessionMap.get(id).getTopic();
	}
	
	@Override
	public Topic getTopic(T obj) {
		return this.getTopic(obj.getId());
	}
	
	@Override
	public TopicSession getSession(Long id) {
		return this.topicSessionMap.get(id).getTopicSession();
	}
	
	@Override
	public TopicSession getSession(T obj) {
		return this.getSession(obj.getId());
	}
	
}
