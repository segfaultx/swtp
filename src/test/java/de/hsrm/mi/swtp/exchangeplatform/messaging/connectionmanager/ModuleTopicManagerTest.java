package de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import de.hsrm.mi.swtp.exchangeplatform.repository.ModuleRepository;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.jms.*;

import static org.junit.Assert.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ModuleTopicManagerTest {
	
	static final Logger log = LoggerFactory.getLogger(ModuleTopicManagerTest.class);
	
	@Autowired
	ModuleRepository moduleRepository;
	@Autowired
	ModuleTopicManager moduleTopicManager;
	@Autowired
	JmsTemplate jmsTopicTemplate;
	
	private Module module;
	private final String testMessage = "TEST MESSAGE";
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	private String getTopicName(Module module) {
		return String.format(DynamicTopicManager.TOPIC_NAME_BASE, "Module", module.getId());
	}
	
	@BeforeEach
	void setUp() {
		module = moduleRepository.findAll().get(0);
	}
	
	@Test
	@Order(0)
	void injectionsNotNull() {
		assertNotNull(moduleRepository);
		assertNotNull(moduleTopicManager);
		assertNotNull(jmsTopicTemplate);
	}
	
	@Test
	@Order(1)
	public void testCreateTopic() {
		Assertions.assertThatCode(() -> {
			Topic topic = moduleTopicManager.createTopic(this.module);
			assertNotNull(topic);
			assertEquals(topic.getTopicName(), getTopicName(this.module));
		}).doesNotThrowAnyException();
	}
	
	@Test
	@Order(2)
	public void testCreateTopicNull() {
		Assertions.assertThatCode(() -> {
			Topic topic = moduleTopicManager.createTopic(null);
			assertNull(topic);
		});
	}
	
	@Test
	@Order(3)
	public void testCreateTopicNullId() {
		Assertions.assertThatCode(() -> {
			this.module.setId(null);
			Topic topic = moduleTopicManager.createTopic(this.module);
			assertNull(topic);
		}).doesNotThrowAnyException();
	}
	
	@Test
	@Order(4)
	public void testGetTopic() {
		Assertions.assertThatCode(() -> {
			Topic topic = moduleTopicManager.getTopic(this.module);
			assertNotNull(topic);
			assertEquals(topic.getTopicName(), getTopicName(this.module));
		}).doesNotThrowAnyException();
	}
	
	@Test
	@Order(5)
	public void testGetTopicNull() {
		Assertions.assertThatCode(() -> {
			this.module.setId(null);
			Topic topic = moduleTopicManager.getTopic(this.module);
			assertNull(topic);
		}).doesNotThrowAnyException();
	}
	
	@Test
	@Order(6)
	public void testGetTopicById() {
		Assertions.assertThatCode(() -> {
			Topic topic = moduleTopicManager.getTopic(this.module.getId());
			assertNotNull(topic);
			assertEquals(topic.getTopicName(), getTopicName(this.module));
		}).doesNotThrowAnyException();
	}
	
	@Test
	@Order(7)
	public void testGetTopicByIdNull() {
		Assertions.assertThatCode(() -> {
			this.module.setId(null);
			Topic topic = moduleTopicManager.getTopic(this.module.getId());
			assertNull(topic);
		}).doesNotThrowAnyException();
	}
	
	@Test
	@Order(8)
	public void testGetSession() {
		Assertions.assertThatCode(() -> {
			TopicSession session = moduleTopicManager.getSession(this.module);
			assertNotNull(session);
		}).doesNotThrowAnyException();
	}
	
	@Test
	@Order(9)
	public void testGetSessionNull() {
		Assertions.assertThatCode(() -> {
			this.module.setId(null);
			TopicSession session = moduleTopicManager.getSession(this.module);
			assertNull(session);
		}).doesNotThrowAnyException();
	}
	
	@Test
	@Order(10)
	public void testGetSessionById() {
		Assertions.assertThatCode(() -> {
			TopicSession session = moduleTopicManager.getSession(this.module.getId());
			assertNotNull(session);
		}).doesNotThrowAnyException();
	}
	
	@Test
	@Order(11)
	public void testGetSessionByIdNull() {
		Assertions.assertThatCode(() -> {
			this.module.setId(null);
			TopicSession session = moduleTopicManager.getSession(this.module.getId());
			assertNull(session);
		}).doesNotThrowAnyException();
	}
	
	@Test
	@Order(12)
	public void testCreateTopicAndReceive() {
		Assertions.assertThatCode(() -> {
			Topic topic = moduleTopicManager.createTopic(this.module);
			final String DESTINATION = getTopicName(module);
			TopicSession session = moduleTopicManager.getSession(module);
			TopicSubscriber subscriber = session.createSubscriber(topic);
			assertNotNull(subscriber);
			subscriber.setMessageListener(message -> {
				TextMessage textMessage = (TextMessage) message;
				try {
					log.info(" => TEST RECEIVED: " + textMessage.getText());
					assertEquals(textMessage.getText(), testMessage);
				} catch(JMSException e) {
					e.printStackTrace();
				}
			});
			
			TopicPublisher publisher = session.createPublisher(topic);
			publisher.publish(session.createTextMessage(testMessage));
			assertNotNull(publisher);
		}).doesNotThrowAnyException();
	}
	
}
