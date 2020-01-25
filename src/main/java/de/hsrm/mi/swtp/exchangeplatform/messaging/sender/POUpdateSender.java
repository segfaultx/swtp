package de.hsrm.mi.swtp.exchangeplatform.messaging.sender;


import de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager.PersonalQueueManager;
import de.hsrm.mi.swtp.exchangeplatform.service.admin.po.POUpdateService;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class POUpdateSender {
	
	POUpdateService poUpdateService;
	PersonalQueueManager personalQueueManager;
	
	public void notifyClients() {
//		for()
	}
	
}
