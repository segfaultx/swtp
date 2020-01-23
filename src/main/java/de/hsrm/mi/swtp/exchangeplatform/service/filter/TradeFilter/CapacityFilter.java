package de.hsrm.mi.swtp.exchangeplatform.service.filter.TradeFilter;

import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import de.hsrm.mi.swtp.exchangeplatform.repository.TimeslotRepository;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.Filter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements a filter for TradeOffers based on their capacity
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
public class CapacityFilter implements Filter {
	
	@Autowired
	TimeslotRepository timeslotRepository;
	
	/**
	 * filters TradeOffers for timeslots that havent reached their max capacity (allowing for instant trade later on)
	 * @param offers list of offers
	 * @return all TradeOffers with attached timeslots that havent reached their max capacity
	 */
	@Override
    public List<TradeOffer> filter(List<TradeOffer> offers){
        List<TradeOffer> capacityList = new ArrayList<>();
        for(TradeOffer offer : offers){
        	// compare max capacity to number of already subscribed attendees
            if(offer.getSeek().getCapacity() > offer.getSeek().getAttendees().size()){
                capacityList.add(offer);
            }
        }
        return capacityList;
    }
}