package de.hsrm.mi.swtp.exchangeplatform.service.filter.TradeFilter;

import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.Filter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * check if TradeOffers exist
 */
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OfferFilter implements Filter {
	
	String filterName = "OfferFilter";
	
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
	
	@Override
	public String getFilterName() {
		return filterName;
	}
}