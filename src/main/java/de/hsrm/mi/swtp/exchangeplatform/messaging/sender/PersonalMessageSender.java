package de.hsrm.mi.swtp.exchangeplatform.messaging.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.hsrm.mi.swtp.exchangeplatform.messaging.PersonalQueue;
import de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager.PersonalQueueManager;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.LeaveModuleSuccessfulMessage;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.LeaveTimeslotSuccessfulMessage;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.TradeOfferSuccessfulMessage;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.service.admin.po.filter.UserOccupancyViolation;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.UserService;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;

/**
 * A simple class which will provide methods for sending messages to a specific personal queue of a {@link User}.
 */
@Slf4j
@Component("personalMessageSender")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PersonalMessageSender {
	
	PersonalQueueManager personalQueueManager;
	JmsTemplate jmsTemplate;
	ObjectMapper objectMapper;
	UserService userService;
	
	public void send(Long userId, TradeOfferSuccessfulMessage tradeOfferSuccessfulMessage) {
		ActiveMQQueue queue = personalQueueManager.getQueue(userId);
		if(queue == null){
			try {
				queue = personalQueueManager.createQueueForOfflineUser(userService.getById(userId).get());
			} catch(JMSException e) {
				return;
			}
		}
		this.send(queue, tradeOfferSuccessfulMessage);
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
		PersonalQueue personalQueue = personalQueueManager.getPersonalQueue(student);
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
	
	public void send(User student, LeaveModuleSuccessfulMessage leaveModuleSuccessfulMessage) {
		final ActiveMQQueue queue = personalQueueManager.getPersonalQueue(student, true);
		jmsTemplate.send(queue,
						 session -> {
							 try {
								 return session.createTextMessage(objectMapper.writeValueAsString(leaveModuleSuccessfulMessage));
							 } catch(JsonProcessingException e) {
								 e.printStackTrace();
							 }
							 return session.createTextMessage("{}");
						 });
		log.info("SEND TO ONLINE USER::" + queue.getQualifiedName() + "::MSG=" + leaveModuleSuccessfulMessage);
	}
	
	public void send(User student, LeaveTimeslotSuccessfulMessage leaveTimeslotSuccessfulMessage) {
		final ActiveMQQueue queue = personalQueueManager.getPersonalQueue(student, true);
		jmsTemplate.send(queue,
						 session -> {
							 try {
								 return session.createTextMessage(objectMapper.writeValueAsString(leaveTimeslotSuccessfulMessage));
							 } catch(JsonProcessingException e) {
								 e.printStackTrace();
							 }
							 return session.createTextMessage("{}");
						 });
		log.info("SEND TO ONLINE USER::" + queue.getQualifiedName() + "::MSG=" + leaveTimeslotSuccessfulMessage);
	}
	
}