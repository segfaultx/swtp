package de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.repository.TradeOfferRepository;
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
public class TradeOfferTopicManagerTest {
	
	static final Logger log = LoggerFactory.getLogger(TradeOfferTopicManagerTest.class);
	
	@Autowired
	TradeOfferRepository tradeOfferRepository;
	@Autowired
	TradeOfferTopicManager tradeOfferTopicManager;
	@Autowired
	JmsTemplate jmsTopicTemplate;
	
	private TradeOffer tradeOffer;
	private final String testMessage = "TEST MESSAGE";
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	private String getTopicName(TradeOffer tradeOffer) {
		return String.format(DynamicTopicManager.TOPIC_NAME_BASE, "TradeOffer", tradeOffer.getId());
	}
	
	@BeforeEach
	void setUp() {
		try {
			tradeOffer = tradeOfferRepository.findAll().get(0);
		} catch(IndexOutOfBoundsException e) {
			tradeOffer = new TradeOffer();
			tradeOffer.setAccepted(false);
			tradeOffer.setId(1234L);
			tradeOffer.setInstantTrade(true);
			tradeOffer.setOffer(new Timeslot());
			tradeOffer.setOfferer(new User());
			tradeOffer.setSeek(new Timeslot());
			
			this.tradeOfferTopicManager.createTopic(tradeOffer);
		}
	}
	
	@Test
	@Order(0)
	void injectionsNotNull() {
		assertNotNull(tradeOfferRepository);
		assertNotNull(tradeOfferTopicManager);
		assertNotNull(jmsTopicTemplate);
	}
	
	@Test
	@Order(1)
	public void testCreateTopic() {
		Assertions.assertThatCode(() -> {
			Topic topic = tradeOfferTopicManager.createTopic(this.tradeOffer);
			assertNotNull(topic);
			assertEquals(topic.getTopicName(), getTopicName(this.tradeOffer));
		}).doesNotThrowAnyException();
	}
	
	@Test
	@Order(2)
	public void testCreateTopicNull() {
		Assertions.assertThatCode(() -> {
			Topic topic = tradeOfferTopicManager.createTopic(null);
			assertNull(topic);
		});
	}
	
	@Test
	@Order(3)
	public void testCreateTopicNullId() {
		Assertions.assertThatCode(() -> {
			this.tradeOffer.setId(null);
			Topic topic = tradeOfferTopicManager.createTopic(this.tradeOffer);
			assertNull(topic);
		}).doesNotThrowAnyException();
	}
	
	@Test
	@Order(4)
	public void testGetTopic() {
		Assertions.assertThatCode(() -> {
			Topic topic = tradeOfferTopicManager.getTopic(this.tradeOffer);
			assertNotNull(topic);
			assertEquals(topic.getTopicName(), getTopicName(this.tradeOffer));
		}).doesNotThrowAnyException();
	}
	
	@Test
	@Order(5)
	public void testGetTopicNull() {
		Assertions.assertThatCode(() -> {
			this.tradeOffer.setId(null);
			Topic topic = tradeOfferTopicManager.getTopic(this.tradeOffer);
			assertNull(topic);
		}).doesNotThrowAnyException();
	}
	
	@Test
	@Order(6)
	public void testGetTopicById() {
		Assertions.assertThatCode(() -> {
			Topic topic = tradeOfferTopicManager.getTopic(this.tradeOffer.getId());
			assertNotNull(topic);
			assertEquals(topic.getTopicName(), getTopicName(this.tradeOffer));
		}).doesNotThrowAnyException();
	}
	
	@Test
	@Order(7)
	public void testGetTopicByIdNull() {
		Assertions.assertThatCode(() -> {
			this.tradeOffer.setId(null);
			Topic topic = tradeOfferTopicManager.getTopic(this.tradeOffer.getId());
			assertNull(topic);
		}).doesNotThrowAnyException();
	}
	
	@Test
	@Order(8)
	public void testGetSession() {
		Assertions.assertThatCode(() -> {
			TopicSession session = tradeOfferTopicManager.getSession(this.tradeOffer);
			assertNotNull(session);
		}).doesNotThrowAnyException();
	}
	
	@Test
	@Order(9)
	public void testGetSessionNull() {
		Assertions.assertThatCode(() -> {
			this.tradeOffer.setId(null);
			TopicSession session = tradeOfferTopicManager.getSession(this.tradeOffer);
			assertNull(session);
		}).doesNotThrowAnyException();
	}
	
	@Test
	@Order(10)
	public void testGetSessionById() {
		Assertions.assertThatCode(() -> {
			TopicSession session = tradeOfferTopicManager.getSession(this.tradeOffer.getId());
			assertNotNull(session);
		}).doesNotThrowAnyException();
	}
	
	@Test
	@Order(11)
	public void testGetSessionByIdNull() {
		Assertions.assertThatCode(() -> {
			this.tradeOffer.setId(null);
			TopicSession session = tradeOfferTopicManager.getSession(this.tradeOffer.getId());
			assertNull(session);
		}).doesNotThrowAnyException();
	}
	
	@Test
	@Order(12)
	public void testCreateTopicAndReceive() {
		Assertions.assertThatCode(() -> {
			Topic topic = tradeOfferTopicManager.createTopic(this.tradeOffer);
			final String DESTINATION = getTopicName(tradeOffer);
			TopicSession session = tradeOfferTopicManager.getSession(tradeOffer);
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
