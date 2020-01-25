package de.hsrm.mi.swtp.exchangeplatform.service.filter.TradeFilter;

import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * check if TradeOffers exist
 */
public class OfferFilter implements Filter {
	
	/**
	 * Check for all valid Offers (Method viable to change!)
	 * @param offers list of TradeOffers
	 * @return all valid TradeOffers
	 */
	@Override
    public List<TradeOffer> doFilter(List<TradeOffer> offers){
        List<TradeOffer> offerList = new ArrayList<>();
        if(offers.size() > 0){
            return offers;
        }
        return offerList;
    }
}