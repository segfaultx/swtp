package de.hsrm.mi.swtp.exchangeplatform.service.filter.TradeFilter;

import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.Filter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Checks if there are any offers yet
 */
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NoOfferFilter implements Filter {
	
	
	String filterName = "NoOfferFilter";
	/**
	 * checks the offers, returns empty list
 	 * @param offers
	 * @return empty list
	 */
	@Override
    public List<TradeOffer> doFilter(List<TradeOffer> offers) throws RuntimeException{
        List<TradeOffer> noOfferList = new ArrayList<>();
        /// check if empty
        if(offers == null || offers.isEmpty()){
            return noOfferList;
        }
        return offers;
    }
	
	@Override
	public String getFilterName() {
		return filterName;
	}
}