package de.hsrm.mi.swtp.exchangeplatform.service.filter;

import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;

import java.util.ArrayList;
import java.util.List;

public class OfferFilter implements Filter {
	
	@Override
	public List<TradeOffer> filter(List<TradeOffer> offers) {
		List<TradeOffer> offerList = new ArrayList<>();
		if(offers.size() > 0) {
			return offers;
		}
		
		return offerList;
	}
}