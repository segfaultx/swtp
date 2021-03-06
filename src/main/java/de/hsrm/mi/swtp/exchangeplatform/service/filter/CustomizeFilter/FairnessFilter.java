package de.hsrm.mi.swtp.exchangeplatform.service.filter.CustomizeFilter;

import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.Filter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;

/**
 * Class which implements a filter based on a fairness point system
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FairnessFilter implements Filter {
	
	String filterName = "FairnessFilter";
	
	/**
	 * sorted Tradeoffer list based on the amount of "fairness points" the students have
	 * @param offers list of TradeOffers
	 * @return sorted list of TradeOffers
	 */
	@Override
	public List<TradeOffer> doFilter(List<TradeOffer> offers, User seeker) {
		/// sort by comparing the offerers "fairness points"
		offers.sort(new Comparator<TradeOffer>() {
			@Override
			public int compare(TradeOffer o1, TradeOffer o2) {
				int offer_fp_1 = o1.getOfferer().getFairness();
				int offer_fp_2 = o2.getOfferer().getFairness();
				if (offer_fp_1 == offer_fp_2) {
					return 0;
				} else if (offer_fp_1 == 0) {
					return -1;
				} else if (offer_fp_2 == 0) {
					return 1;
				} else {
					return Integer.compare(offer_fp_1, offer_fp_2);
				}
			}
		});
		/// sorted list
		return offers;
	}
	
	@Override
	public String getFilterName() {
		return filterName;
	}
}
