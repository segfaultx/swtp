package de.hsrm.mi.swtp.exchangeplatform.service.filter.TradeFilter;

import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements a filter for TradeOffers based on their capacity
 */
public class CapacityFilter implements Filter {
	
	/**
	 * filters TradeOffers for timeslots that havent reached their max capacity (allowing for instant trade later on)
	 * @param offers
	 * @return all TradeOffers with attached timeslots that havent reached their max capacity
	 */
	@Override
	public List<TradeOffer> filter(List<TradeOffer> offers){
		List<TradeOffer> capacityList = new ArrayList<>();
		for(TradeOffer offer : capacityList){
			/// compare max capacity to number of already subscribed attendees
			if(offer.getSeek().getCapacity() < offer.getOfferer().getTimeslots().size()){
				capacityList.add(offer);
			}
		}
		return capacityList;
	}
}