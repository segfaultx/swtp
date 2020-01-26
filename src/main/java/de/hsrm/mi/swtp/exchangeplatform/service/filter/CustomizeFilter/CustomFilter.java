package de.hsrm.mi.swtp.exchangeplatform.service.filter.CustomizeFilter;

import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.Filter;

import java.util.List;

/**
 * class which offers the method of chaining multiple filters over a list of TradeOffers
 */
public class CustomFilter {
	
	/**
	 * applies all given filters. Result may vary if multiple filters are selected based on the order of selection.
	 * @param filter List of all selected filters
	 * @param offers List of all given Tradeoffers. This list will be sorted according to the choosen filter.
	 * @return the most viable tradeoffer. The first entry in a list of sorted tradeoffers
	 */
	TradeOffer applyFilter(List<Filter> filter, List<TradeOffer> offers){
		///iterate over the given filters
		for(Filter subFilter : filter){
			offers = subFilter.doFilter(offers);
		}
		/// the first entry of the sorted TradeOffer list
		return offers.get(0);
	}
}
