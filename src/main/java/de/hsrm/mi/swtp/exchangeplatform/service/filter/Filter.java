package de.hsrm.mi.swtp.exchangeplatform.service.filter;

import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;

import java.util.List;

/**
 * Multiple filters may be used throughout the project. Any filter is given a list of TradeOffers and returns a changed list of TradeOffers
 */
public interface Filter {
	
    List<TradeOffer> doFilter(List<TradeOffer> offers, User seeker);
    
    String getFilterName() throws RuntimeException;
}