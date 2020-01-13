package de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager;

import com.github.javafaker.Faker;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TimeTable;
import de.hsrm.mi.swtp.exchangeplatform.repository.TimeTableRepository;
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
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class TimeTableTopicManagerTest {
	
	static final Logger log = LoggerFactory.getLogger(TimeTableTopicManagerTest.class);
	
	@Autowired
	TimeTableRepository timeTableRepository;
	@Autowired
	TimeTableTopicManager timeTableTopicManager;
	@Autowired
	JmsTemplate jmsTopicTemplate;
	
	private TimeTable timeTable;
	private final String testMessage = "TEST MESSAGE";
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	private String getTopicName(TimeTable timeTable) {
		return String.format(DynamicTopicManager.TOPIC_NAME_BASE, "TimeTable", timeTable.getId());
	}
	
	@BeforeEach
	void setUp() {
		try{
			timeTable = timeTableRepository.findAll().get(0);
		} catch(IndexOutOfBoundsException e) {
			Faker faker = Faker.instance();
			Date dateEnd = faker.date().future(300, TimeUnit.DAYS);
			timeTable = new TimeTable();
			timeTable.setDateEnd(Instant.ofEpochMilli(dateEnd.getTime())
										.atZone(ZoneId.systemDefault())
										.toLocalDate());
			timeTable.setDateStart(LocalDate.now());
			timeTable.setId(1234L);
			timeTable.setTimeslots(new ArrayList<>());
			
			this.timeTableTopicManager.createTopic(timeTable);
		}
	}
	
	@Test
	@Order(0)
	void injectionsNotNull() {
		assertNotNull(timeTableRepository);
		assertNotNull(timeTableTopicManager);
		assertNotNull(jmsTopicTemplate);
	}
	
	@Test
	@Order(1)
	public void testCreateTopic() {
		Assertions.assertThatCode(() -> {
			Topic topic = timeTableTopicManager.createTopic(this.timeTable);
			assertNotNull(topic);
			assertEquals(topic.getTopicName(), getTopicName(this.timeTable));
		}).doesNotThrowAnyException();
	}
	
	@Test
	@Order(2)
	public void testCreateTopicNull() {
		Assertions.assertThatCode(() -> {
			Topic topic = timeTableTopicManager.createTopic(null);
			assertNull(topic);
		});
	}
	
	@Test
	@Order(3)
	public void testCreateTopicNullId() {
		Assertions.assertThatCode(() -> {
			this.timeTable.setId(null);
			Topic topic = timeTableTopicManager.createTopic(this.timeTable);
			assertNull(topic);
		}).doesNotThrowAnyException();
	}
	
	@Test
	@Order(4)
	public void testGetTopic() {
		Assertions.assertThatCode(() -> {
			Topic topic = timeTableTopicManager.getTopic(this.timeTable);
			assertNotNull(topic);
			assertEquals(topic.getTopicName(), getTopicName(this.timeTable));
		}).doesNotThrowAnyException();
	}
	
	@Test
	@Order(5)
	public void testGetTopicNull() {
		Assertions.assertThatCode(() -> {
			this.timeTable.setId(null);
			Topic topic = timeTableTopicManager.getTopic(this.timeTable);
			assertNull(topic);
		}).doesNotThrowAnyException();
	}
	
	@Test
	@Order(6)
	public void testGetTopicById() {
		Assertions.assertThatCode(() -> {
			Topic topic = timeTableTopicManager.getTopic(this.timeTable.getId());
			assertNotNull(topic);
			assertEquals(topic.getTopicName(), getTopicName(this.timeTable));
		}).doesNotThrowAnyException();
	}
	
	@Test
	@Order(7)
	public void testGetTopicByIdNull() {
		Assertions.assertThatCode(() -> {
			this.timeTable.setId(null);
			Topic topic = timeTableTopicManager.getTopic(this.timeTable.getId());
			assertNull(topic);
		}).doesNotThrowAnyException();
	}
	
	@Test
	@Order(8)
	public void testGetSession() {
		Assertions.assertThatCode(() -> {
			TopicSession session = timeTableTopicManager.getSession(this.timeTable);
			assertNotNull(session);
		}).doesNotThrowAnyException();
	}
	
	@Test
	@Order(9)
	public void testGetSessionNull() {
		Assertions.assertThatCode(() -> {
			this.timeTable.setId(null);
			TopicSession session = timeTableTopicManager.getSession(this.timeTable);
			assertNull(session);
		}).doesNotThrowAnyException();
	}
	
	@Test
	@Order(10)
	public void testGetSessionById() {
		Assertions.assertThatCode(() -> {
			TopicSession session = timeTableTopicManager.getSession(this.timeTable.getId());
			assertNotNull(session);
		}).doesNotThrowAnyException();
	}
	
	@Test
	@Order(11)
	public void testGetSessionByIdNull() {
		Assertions.assertThatCode(() -> {
			this.timeTable.setId(null);
			TopicSession session = timeTableTopicManager.getSession(this.timeTable.getId());
			assertNull(session);
		}).doesNotThrowAnyException();
	}
	
	@Test
	@Order(12)
	public void testCreateTopicAndReceive() {
		Assertions.assertThatCode(() -> {
			Topic topic = timeTableTopicManager.createTopic(this.timeTable);
			final String DESTINATION = getTopicName(timeTable);
			TopicSession session = timeTableTopicManager.getSession(timeTable);
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
