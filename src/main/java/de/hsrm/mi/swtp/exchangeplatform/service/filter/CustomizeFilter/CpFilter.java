package de.hsrm.mi.swtp.exchangeplatform.service.filter.CustomizeFilter;

import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.repository.UserRepository;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.Filter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;

/**
 * Implements a filter based on student cp
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CpFilter implements Filter
{
	String filterName = "CpFilter";
	UserRepository userRepository;
	
	public CpFilter(UserRepository userRepository) {this.userRepository = userRepository;}
	
	/**
	 * sorted Tradeoffer list based on the amount of cps the students have
	 * @param offers List of tradeoffers which includes data on students cps
	 * @return sorted list of tradeoffers
	 */
	@Override
	public List<TradeOffer> doFilter(List<TradeOffer> offers, User seeker) {
		/// sort by comparing the offerers cps
		offers.sort(new Comparator<TradeOffer>() {
			@Override
			public int compare(TradeOffer o1, TradeOffer o2) {
				int offer_cp_1 = o1.getOfferer().getCp();
				int offer_cp_2 = o2.getOfferer().getCp();
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
	
	@Override
	public String getFilterName() {
		return filterName;
	}
}
