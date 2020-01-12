package de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.repository.TimeslotRepository;
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
public class TimeslotTopicManagerTest {
	
	static final Logger log = LoggerFactory.getLogger(TimeslotTopicManagerTest.class);
	
	@Autowired
	TimeslotRepository timeslotRepository;
	@Autowired
	TimeslotTopicManager timeslotTopicManager;
	@Autowired
	JmsTemplate jmsTopicTemplate;
	
	private Timeslot timeslot;
	private final String testMessage = "TEST MESSAGE";
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	private String getTopicName(Timeslot timeslot) {
		return String.format(DynamicTopicManager.TOPIC_NAME_BASE, "Timeslot", timeslot.getId());
	}
	
	@BeforeEach
	void setUp() {
		timeslot = timeslotRepository.findAll().get(0);
	}
	
	@Test
	@Order(0)
	void injectionsNotNull() {
		assertNotNull(timeslotRepository);
		assertNotNull(timeslotTopicManager);
		assertNotNull(jmsTopicTemplate);
	}
	
	@Test
	@Order(1)
	public void testCreateTopic() {
		Assertions.assertThatCode(() -> {
			Topic topic = timeslotTopicManager.createTopic(this.timeslot);
			assertNotNull(topic);
			assertEquals(topic.getTopicName(), getTopicName(this.timeslot));
		}).doesNotThrowAnyException();
	}
	
	@Test
	@Order(2)
	public void testCreateTopicNull() {
		Assertions.assertThatCode(() -> {
			Topic topic = timeslotTopicManager.createTopic(null);
			assertNull(topic);
		});
	}
	
	@Test
	@Order(3)
	public void testCreateTopicNullId() {
		Assertions.assertThatCode(() -> {
			this.timeslot.setId(null);
			Topic topic = timeslotTopicManager.createTopic(this.timeslot);
			assertNull(topic);
		}).doesNotThrowAnyException();
	}
	
	@Test
	@Order(4)
	public void testGetTopic() {
		Assertions.assertThatCode(() -> {
			Topic topic = timeslotTopicManager.getTopic(this.timeslot);
			assertNotNull(topic);
			assertEquals(topic.getTopicName(), getTopicName(this.timeslot));
		}).doesNotThrowAnyException();
	}
	
	@Test
	@Order(5)
	public void testGetTopicNull() {
		Assertions.assertThatCode(() -> {
			this.timeslot.setId(null);
			Topic topic = timeslotTopicManager.getTopic(this.timeslot);
			assertNull(topic);
		}).doesNotThrowAnyException();
	}
	
	@Test
	@Order(6)
	public void testGetTopicById() {
		Assertions.assertThatCode(() -> {
			Topic topic = timeslotTopicManager.getTopic(this.timeslot.getId());
			assertNotNull(topic);
			assertEquals(topic.getTopicName(), getTopicName(this.timeslot));
		}).doesNotThrowAnyException();
	}
	
	@Test
	@Order(7)
	public void testGetTopicByIdNull() {
		Assertions.assertThatCode(() -> {
			this.timeslot.setId(null);
			Topic topic = timeslotTopicManager.getTopic(this.timeslot.getId());
			assertNull(topic);
		}).doesNotThrowAnyException();
	}
	
	@Test
	@Order(8)
	public void testGetSession() {
		Assertions.assertThatCode(() -> {
			TopicSession session = timeslotTopicManager.getSession(this.timeslot);
			assertNotNull(session);
		}).doesNotThrowAnyException();
	}
	
	@Test
	@Order(9)
	public void testGetSessionNull() {
		Assertions.assertThatCode(() -> {
			this.timeslot.setId(null);
			TopicSession session = timeslotTopicManager.getSession(this.timeslot);
			assertNull(session);
		}).doesNotThrowAnyException();
	}
	
	@Test
	@Order(10)
	public void testGetSessionById() {
		Assertions.assertThatCode(() -> {
			TopicSession session = timeslotTopicManager.getSession(this.timeslot.getId());
			assertNotNull(session);
		}).doesNotThrowAnyException();
	}
	
	@Test
	@Order(11)
	public void testGetSessionByIdNull() {
		Assertions.assertThatCode(() -> {
			this.timeslot.setId(null);
			TopicSession session = timeslotTopicManager.getSession(this.timeslot.getId());
			assertNull(session);
		}).doesNotThrowAnyException();
	}
	
	@Test
	@Order(12)
	public void testCreateTopicAndReceive() {
		Assertions.assertThatCode(() -> {
			Topic topic = timeslotTopicManager.createTopic(this.timeslot);
			final String DESTINATION = getTopicName(timeslot);
			TopicSession session = timeslotTopicManager.getSession(timeslot);
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
