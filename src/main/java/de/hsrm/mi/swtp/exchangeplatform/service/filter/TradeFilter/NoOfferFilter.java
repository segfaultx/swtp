package de.hsrm.mi.swtp.exchangeplatform.service.filter.TradeFilter;

import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * Checks if there are any offers yet
 */
public class NoOfferFilter implements Filter {
	
	/**
	 * checks the offers, returns empty list
 	 * @param offers
	 * @return empty list
	 */
	@Override
    public List<TradeOffer> filter(List<TradeOffer> offers){
        List<TradeOffer> noOfferList = new ArrayList<>();
        /// check if empty
        if(offers.isEmpty()){
            return noOfferList;
        }
        return offers;
    }
}