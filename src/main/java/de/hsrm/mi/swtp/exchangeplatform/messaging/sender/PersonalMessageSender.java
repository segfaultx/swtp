package de.hsrm.mi.swtp.exchangeplatform.messaging.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.hsrm.mi.swtp.exchangeplatform.messaging.PersonalConnection;
import de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager.PersonalConnectionManager;
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
	
	PersonalConnectionManager personalConnectionManager;
	JmsTemplate jmsTemplate;
	ObjectMapper objectMapper;
	
	public void send(User user, TradeOfferSuccessfulMessage tradeOfferSuccessfulMessage) {
		this.send(personalConnectionManager.getQueue(user), tradeOfferSuccessfulMessage);
	}
	
	public void send(Long userId, TradeOfferSuccessfulMessage tradeOfferSuccessfulMessage) {
		this.send(personalConnectionManager.getQueue(userId), tradeOfferSuccessfulMessage);
	}
	
	public void send(ActiveMQQueue userQueue, TradeOfferSuccessfulMessage tradeOfferSuccessfulMessage) {
		jmsTemplate.send(userQueue, session -> {
			try {
				return session.createTextMessage(objectMapper.writeValueAsString(tradeOfferSuccessfulMessage));
			} catch(JsonProcessingException e) {
				e.printStackTrace();
			}
			return session.createTextMessage("{}");
		});
	}
	
	public void send(UserOccupancyViolation userOccupancyViolation) {
		User student = userOccupancyViolation.getStudent();
		PersonalConnection personalConnection = personalConnectionManager.getPersonalConnection(student);
		if(personalConnection == null) {
			// user is not logged in; so send message which the user can receive later
			jmsTemplate.send(personalConnectionManager.createPersonalQueueName(student),
							 session -> {
								 try {
									 return session.createTextMessage(objectMapper.writeValueAsString(userOccupancyViolation));
								 } catch(JsonProcessingException e) {
									 e.printStackTrace();
								 }
								 return session.createTextMessage("{\"violations\": \"true\"}");
							 });
			log.info("SEND TO OFFLINE USER::" + personalConnectionManager.createPersonalQueueName(student) + "::MSG=" + userOccupancyViolation);
			return;
		}
		jmsTemplate.send(personalConnection.getPersonalQueue(),
						 session -> {
							 try {
								 return session.createTextMessage(objectMapper.writeValueAsString(userOccupancyViolation));
							 } catch(JsonProcessingException e) {
								 e.printStackTrace();
							 }
							 return session.createTextMessage("{\"violations\": \"true\"}");
						 });
		log.info("SEND TO ONLINE USER::" + personalConnection.getPersonalQueue().getQualifiedName() + "::MSG=" + userOccupancyViolation);
	}
	
}