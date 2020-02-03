package de.hsrm.mi.swtp.exchangeplatform.service.filter.TradeFilter;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.repository.UserRepository;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.Filter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * checks a students timetable for collision with given offers
 */
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CollisionFilter implements Filter {
	
	String filterName = "CollisionFilter";
	

	/**
	 * checks if a students timetable collides with the given TradeOffers (prohibiting a future trade unless resolved)
	 * @param offers list of TradeOffers
	 * @return a list of all TradeOffers which collide
	 */
	@Override
    public List<TradeOffer> doFilter(List<TradeOffer> offers,User seeker){
        List<TradeOffer> collisionList = new ArrayList<>(offers);
        
        for(TradeOffer offer : offers) {
            for (Timeslot timeslot : seeker.getTimeslots()) {
            	/// compare all filled timeslots of a student with all TradeOffers
                if ((offer.getOffer().getDay() == timeslot.getDay()) && (checkCollision(offer.getOffer(),timeslot))) {
                    collisionList.remove(offer);
                    break;
                }
            }
        }
        return collisionList;
    }
	
	@Override
	public String getFilterName() {
		return filterName;
	}
	
	/**
	 * Check for collisions, timeslot durations might differ. Method has to check all possibilities
	 * @param offer timeslot user wants
	 * @param filled timeslot user has (all of his timeslots must be compared to offer)
	 * @return true for collision, else false
	 */
	public boolean checkCollision(Timeslot offer, Timeslot filled){
		LocalTime aTimeStart = offer.getTimeStart();
		LocalTime aTimeEnd = offer.getTimeEnd();
		LocalTime bTimeStart = filled.getTimeStart();
		LocalTime bTimeEnd = filled.getTimeEnd();
		
		
	if(aTimeStart.equals(bTimeStart) || aTimeEnd.equals(bTimeEnd)) return true;
		if((aTimeStart.isBefore(bTimeEnd) && aTimeStart.isAfter(bTimeStart)) ||
				(bTimeStart.isBefore(aTimeEnd) && bTimeStart.isAfter(aTimeStart))) return true;
		return (aTimeStart.isBefore(bTimeStart)
				&& aTimeEnd.isBefore(bTimeEnd)
				&& bTimeStart.isBefore(aTimeEnd))
				||
				(bTimeStart.isBefore(aTimeStart)
						&& bTimeEnd.isBefore(aTimeEnd)
						&& aTimeStart.isBefore(bTimeEnd));
}
	
}