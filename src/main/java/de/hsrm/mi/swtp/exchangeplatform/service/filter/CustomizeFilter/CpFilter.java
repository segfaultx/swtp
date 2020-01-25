package de.hsrm.mi.swtp.exchangeplatform.service.filter.CustomizeFilter;

import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.Filter;

import java.util.Comparator;
import java.util.List;

/**
 * Implements a filter based on student cp
 */
public class CpFilter implements Filter
{
	/**
	 * sorted Tradeoffer list based on the amount of cps the students have
	 * @param offers List of tradeoffers which includes data on students cps
	 * @return sorted list of tradeoffers
	 */
	@Override
	public List<TradeOffer> doFilter(List<TradeOffer> offers) {
		/// sort by comparing the offerers cps
		offers.sort(new Comparator<TradeOffer>() {
			@Override
			public int compare(TradeOffer o1, TradeOffer o2) {
				int offer_cp_1 = o1.getSeeker().getCp();
				int offer_cp_2 = o2.getSeeker().getCp();
				if (offer_cp_1 == offer_cp_2) {
					return 0;
				} else if (offer_cp_1 == 0) {
					return -1;
				} else if (offer_cp_2 == 0) {
					return 1;
				} else {
					return Integer.compare(offer_cp_1, offer_cp_2);
				}
			}
		});
		
		return offers;
	}
}
