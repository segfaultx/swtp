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
            for (Timeslot timeslot : offer.getSeeker().getTimeslots()) {
            	/// compare all filled timeslots of a student with all TradeOffers
                if (checkCollision(offer.getSeek(),timeslot)) {
                    collisionList.add(offer);
                }
            }
        }
        return collisionList;
    }
    
    
    public boolean checkCollision(Timeslot offer, Timeslot filled){
		//check for same day
		if(offer.getDay() == filled.getDay()) {
			
			if(offer.getTimeStart() == filled.getTimeStart()) {
				return true;
			} else if(offer.getTimeStart().isBefore(filled.getTimeStart()) && offer.getTimeEnd().isBefore(filled.getTimeEnd())) {
				return true;
			} else if(offer.getTimeStart().isBefore(filled.getTimeStart()) && offer.getTimeEnd().isAfter(filled.getTimeEnd())) {
				return true;
			} else if(offer.getTimeStart().isAfter(filled.getTimeStart()) && offer.getTimeEnd().isBefore(filled.getTimeEnd())) {
				return true;
			} else if(offer.getTimeStart().isAfter(filled.getTimeStart()) && offer.getTimeStart().isBefore(filled.getTimeEnd()) && offer.getTimeEnd()
																																		.isAfter(
																																				filled.getTimeEnd())) {
				return true;
			}
			
			return false;
		}
		
		return false;
	}
	
}