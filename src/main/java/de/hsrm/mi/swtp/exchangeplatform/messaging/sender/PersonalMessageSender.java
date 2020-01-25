package de.hsrm.mi.swtp.exchangeplatform.messaging.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.hsrm.mi.swtp.exchangeplatform.messaging.PersonalQueue;
import de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager.PersonalQueueManager;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.TradeOfferSuccessfulMessage;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.service.admin.po.filter.UserOccupancyViolation;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 * A simple class which will provide methods for sending messages to a specific personal queue of a {@link User}.
 */
@Slf4j
@Component
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PersonalMessageSender {
	
	PersonalQueueManager personalQueueManager;
	JmsTemplate jmsTemplate;
	ObjectMapper objectMapper;
	
	public void send(User user, TradeOfferSuccessfulMessage tradeOfferSuccessfulMessage) {
		this.send(personalQueueManager.getQueue(user), tradeOfferSuccessfulMessage);
	}
	
	public void send(Long userId, TradeOfferSuccessfulMessage tradeOfferSuccessfulMessage) {
		this.send(personalQueueManager.getQueue(userId), tradeOfferSuccessfulMessage);
	}
	
	public void send(ActiveMQQueue userQueue, TradeOfferSuccessfulMessage tradeOfferSuccessfulMessage) {
		jmsTemplate.send(userQueue, session -> session.createTextMessage(tradeOfferSuccessfulMessage.toString()));
	}
	
	public void send(UserOccupancyViolation userOccupancyViolation) {
		User student = userOccupancyViolation.getStudent();
		PersonalQueue personalQueue = personalQueueManager.getPersonalConnection(student);
		if(personalQueue == null) {
			// user is not logged in; so send message which the user can receive later
			jmsTemplate.send(personalQueueManager.createPersonalQueueName(student),
							 session -> {
								 try {
									 return session.createTextMessage(objectMapper.writeValueAsString(userOccupancyViolation));
								 } catch(JsonProcessingException e) {
									 e.printStackTrace();
								 }
								 return session.createTextMessage("{\"violations\": \"true\"}");
							 });
			log.info("SEND TO OFFLINE USER::" + personalQueueManager.createPersonalQueueName(student) + "::MSG=" + userOccupancyViolation);
			return;
		}
		jmsTemplate.send(personalQueue.getPersonalQueue(),
						 session -> {
							 try {
								 return session.createTextMessage(objectMapper.writeValueAsString(userOccupancyViolation));
							 } catch(JsonProcessingException e) {
								 e.printStackTrace();
							 }
							 return session.createTextMessage("{\"violations\": \"true\"}");
						 });
		log.info("SEND TO ONLINE USER::" + personalQueue.getPersonalQueue().getQualifiedName() + "::MSG=" + userOccupancyViolation);
	}
	
}