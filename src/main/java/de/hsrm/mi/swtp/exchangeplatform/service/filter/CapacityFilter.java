package de.hsrm.mi.swtp.exchangeplatform.service.filter;

import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;

import java.util.ArrayList;
import java.util.List;

public class CapacityFilter implements Filter {
	
	@Override
	public List<TradeOffer> filter(List<TradeOffer> offers) {
		List<TradeOffer> capacityList = new ArrayList<>();
		for(TradeOffer offer : capacityList) {
			if(offer.getSeek().getCapacity() < offer.getSeek().getAttendees().size()) {
				capacityList.add(offer);
			}
		}
		return capacityList;
	}
}