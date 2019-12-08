package de.hsrm.mi.swtp.exchangeplatform.service.filter;

import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;

import java.util.List;

public interface Filter {
	
	List<TradeOffer> filter(List<TradeOffer> offers);
}