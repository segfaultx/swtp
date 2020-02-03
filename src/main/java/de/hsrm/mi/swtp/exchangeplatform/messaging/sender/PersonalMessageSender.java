package de.hsrm.mi.swtp.exchangeplatform.messaging.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.hsrm.mi.swtp.exchangeplatform.messaging.PersonalQueue;
import de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager.PersonalQueueManager;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.*;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.admin.po.violation.UserOccupancyViolationMessage;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.UserService;
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
@Component("personalMessageSender")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PersonalMessageSender {
	
	PersonalQueueManager personalQueueManager;
	JmsTemplate jmsTemplate;
	ObjectMapper objectMapper;
	UserService userService;
	
	public void send(User student, Message message) {
		final ActiveMQQueue queue = personalQueueManager.getPersonalQueue(student, true);
		jmsTemplate.send(queue,
						 session -> {
							 try {
								 return session.createTextMessage(objectMapper.writeValueAsString(message));
							 } catch(JsonProcessingException e) {
								 e.printStackTrace();
							 }
							 return session.createTextMessage("{}");
						 });
		log.info("SEND TO ONLINE USER::" + queue.getQualifiedName() + "::MSG=" + message);
	}
	
	public void send(User student, TradeOfferSuccessfulMessage tradeOfferSuccessfulMessage) {
		ActiveMQQueue queue = personalQueueManager.getPersonalQueue(student, true);
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
	
	public void send(UserOccupancyViolationMessage userOccupancyViolationMessage) {
		User student = userOccupancyViolationMessage.getStudent();
		PersonalQueue personalQueue = personalQueueManager.getPersonalQueue(student);
		if(personalQueue == null) {
			// user is not logged in; so send message which the user can receive later
			jmsTemplate.send(personalQueueManager.createPersonalQueueName(student),
							 session -> {
								 try {
									 return session.createTextMessage(objectMapper.writeValueAsString(userOccupancyViolationMessage));
								 } catch(JsonProcessingException e) {
									 e.printStackTrace();
								 }
								 return session.createTextMessage("{\"violations\": \"true\"}");
							 });
			log.info("│ › SENT TO OFFLINE USER::" + personalQueueManager.createPersonalQueueName(student) + "::MSG=" + userOccupancyViolationMessage);
			return;
		}
		jmsTemplate.send(personalQueue.getPersonalQueue(),
						 session -> {
							 try {
								 return session.createTextMessage(objectMapper.writeValueAsString(userOccupancyViolationMessage));
							 } catch(JsonProcessingException e) {
								 e.printStackTrace();
							 }
							 return session.createTextMessage("{\"violations\": \"true\"}");
						 });
		log.info("SEND TO ONLINE USER::" + personalQueue.getPersonalQueue().getQualifiedName() + "::MSG=" + userOccupancyViolationMessage);
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
	
	public void send(User student, JoinModuleSuccessfulMessage joinModuleSuccessfulMessage) {
		final ActiveMQQueue queue = personalQueueManager.getPersonalQueue(student, true);
		jmsTemplate.send(queue,
						 session -> {
							 try {
								 return session.createTextMessage(objectMapper.writeValueAsString(joinModuleSuccessfulMessage));
							 } catch(JsonProcessingException e) {
								 e.printStackTrace();
							 }
							 return session.createTextMessage("{}");
						 });
		log.info("SEND TO ONLINE USER::" + queue.getQualifiedName() + "::MSG=" + joinModuleSuccessfulMessage);
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
	
	public void send(User student, JoinTimeslotSuccessfulMessage leaveTimeslotSuccessfulMessage) {
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