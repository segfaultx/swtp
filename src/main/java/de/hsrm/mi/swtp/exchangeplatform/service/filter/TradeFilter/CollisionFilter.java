package de.hsrm.mi.swtp.exchangeplatform.service.filter.TradeFilter;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.TypeOfTimeslots;
import de.hsrm.mi.swtp.exchangeplatform.repository.UserRepository;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.Filter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * checks a students timetable for collision with given offers
 */
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CollisionFilter implements Filter {
	
	String filterName = "CollisionFilter";
	
	@Autowired
	UserRepository userRepository;
	
	public CollisionFilter(UserRepository userRepository) {this.userRepository = userRepository;}
	{}
	/**
	 * checks if a students timetable collides with the given TradeOffers (prohibiting a future trade unless resolved)
	 * @param offers list of TradeOffers
	 * @return a list of all TradeOffers which collide
	 */
	@Override
    public List<TradeOffer> doFilter(List<TradeOffer> offers,User seeker){
        List<TradeOffer> collisionList = new ArrayList<>();
        for(TradeOffer offer : offers) {
            for (Timeslot timeslot : seeker.getTimeslots()) {
            	/// compare all filled timeslots of a student with all TradeOffers
                if (checkCollision(offer.getSeek(),timeslot)) {
                    collisionList.add(offer);
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
		LocalTime offerStart = offer.getTimeStart();
		LocalTime offerEnd = offer.getTimeEnd();
		LocalTime filledStart = filled.getTimeStart();
		LocalTime filledEnd = filled.getTimeEnd();
		
		if((offer.getTimeSlotType().equals(TypeOfTimeslots.PRAKTIKUM) && filled.getTimeSlotType().equals(TypeOfTimeslots.PRAKTIKUM))){
			//check for same day, if yes, check all possible crossovers:
			if(offer.getDay() != filled.getDay()) {
				return false;
				// starts at the same time
			}else if(offerStart == filledStart || offerEnd == filledEnd) return true;
				// starts before filled but ends before filled is over
			else if(filledStart.isAfter(offerStart) && filledEnd.isAfter(offerEnd)) return true;
				// starts before filled and ends after
			else if(offerStart.isBefore(filledStart) && offerEnd.isAfter(filledEnd)) return true;
				// starts after filled but ends before filled is over
			else if(offerStart.isAfter(filledStart) && offerEnd.isBefore(filledEnd)) return true;
				// starts after filled begins and ends after filled is over
			else if(offerStart.isAfter(filledStart) && offerStart.isBefore(filledEnd) && offerEnd.isAfter(filledEnd)) return true;
			
			else return false;
	}
		return false;
}
	
}