package de.hsrm.mi.swtp.exchangeplatform.messaging;

import de.hsrm.mi.swtp.exchangeplatform.messaging.sender.AdminTopicMessageSender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext
public class AdminTopicMessageSenderTest {
	
	@Autowired
	AdminTopicMessageSender adminTopicMessageSender;
	
	@Test
	public void testAddAttendeeToTimeslotUserIsAlreadyAttendee() {
		
		adminTopicMessageSender.send(null);
	}
	
}
