package de.hsrm.mi.swtp.exchangeplatform.service.filter;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;

import java.util.ArrayList;
import java.util.List;

public class CollisionFilter implements Filter {
	
	@Override
	public List<TradeOffer> filter(List<TradeOffer> offers) {
		List<TradeOffer> collisionList = new ArrayList<>();
		for(TradeOffer offer : offers) {
			for(Timeslot timeslot : offer.getOfferer().getTimeslots()) {
				if(timeslot.getTimeStart() == offer.getSeek().getTimeStart()) {
					collisionList.add(offer);
				}
			}
		}
		return collisionList;
	}
}