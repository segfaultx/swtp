package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface TradeService {
	
	boolean doTrade(long studentId, long offeredId, long wantedId) throws NotFoundException;
}
