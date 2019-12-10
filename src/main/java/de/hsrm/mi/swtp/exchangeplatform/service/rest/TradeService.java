package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import org.springframework.stereotype.Service;

@Service
public interface TradeService {
	
	boolean doTrade(long studentId, long offeredId, long wantedId);
}
