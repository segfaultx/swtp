package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import org.springframework.stereotype.Service;

/**
 * Interface for any tradeService implementation
 * @author amatus
 */
@Service
public interface TradeService {
	
	boolean doTrade(User student1, User student2, Timeslot timeslot1, Timeslot timeslot2) throws NotFoundException;
}
