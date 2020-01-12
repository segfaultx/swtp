package de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager;

import de.hsrm.mi.swtp.exchangeplatform.configuration.messaging.DynamicDestinationConfig;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.util.HashMap;

/**
 * Can create a connection for each {@link Module}.
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class ModuleTopicManager extends AbstractDynamicTopicManager<Module> {
	
	/**
	 * @see DynamicDestinationConfig#moduleConnection()
	 */
	TopicConnection moduleConnection;
	/**
	 * A collection which saves all dynamically created Topics of {@link Module Modules} and maps their
	 * {@link Module#getId()} to the corresponding{@link Topic}.
	 * <p>
	 * <Long, Topic> := Long -> {@link Module#}, Topic -> Topic of Module
	 */
	HashMap<Long, Topic> moduleTopicHashMap;
	
	@Autowired
	@Builder
	public ModuleTopicManager(final TopicConnection moduleConnection
							 ) {
		this.moduleConnection = moduleConnection;
		this.moduleTopicHashMap = new HashMap<>();
	}
	
	@Override
	public Topic createTopic(Module obj) {
		Topic topic = null;
		try {
			final String topicName = createTopicName(obj.getId());
			log.info(String.format(" + created topic name: %s", topicName));
			TopicSession topicSession = moduleConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			topic = topicSession.createTopic(topicName);
			log.info(String.format(" + created topic: %s", topic.toString()));
			this.moduleTopicHashMap.put(obj.getId(), topic);
			
			topicSession.createSubscriber(topic).setMessageListener(message -> {
				try {
					log.info(String.format("-> Module %s received: \"%s\"", obj.getId(), ((TextMessage) message).getText()));
				} catch(JMSException e) {
					e.printStackTrace();
				}
			});
			topicSession.createPublisher(topic)
						.publish(topicSession.createTextMessage(String.format("Module %s: Topic %s is now alive", obj.getId(), topicName)));
			
		} catch(JMSException e) {
			log.error(String.format("Could not create Topic for Module %s.", obj.getId()));
			return null;
		}
		return topic;
	}
	
}
