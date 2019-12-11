package de.hsrm.mi.swtp.exchangeplatform.service.filter.TradeFilter;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * checks a students timetable for collision with given offers
 */
public class CollisionFilter implements Filter {
	
	/**
	 * checks if a students timetable collides with the given TradeOffers (prohibiting a future trade unless resolved)
	 * @param offers list of TradeOffers
	 * @return a list of all TradeOffers which collide
	 */
	@Override
    public List<TradeOffer> filter(List<TradeOffer> offers){
        List<TradeOffer> collisionList = new ArrayList<>();
        for(TradeOffer offer : offers) {
            for (Timeslot timeslot : offer.getOfferer().getTimeslots()) {
            	/// compare all filled timeslots of a student with all TradeOffers
                if (timeslot.getTimeStart() == offer.getSeek().getTimeStart()) {
                    collisionList.add(offer);
                }
            }
        }
        return collisionList;
    }
}