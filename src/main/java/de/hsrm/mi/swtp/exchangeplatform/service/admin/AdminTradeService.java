package de.hsrm.mi.swtp.exchangeplatform.service.admin;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import org.springframework.stereotype.Service;

@Service
public interface AdminTradeService {
	
	Timeslot assignTimeslotToStudent(long StudentId, long ownedTimeslot, long futureTimeslot, long adminId);
}
