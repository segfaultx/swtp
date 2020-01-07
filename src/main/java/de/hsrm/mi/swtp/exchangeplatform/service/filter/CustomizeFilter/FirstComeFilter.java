package de.hsrm.mi.swtp.exchangeplatform.service.filter.CustomizeFilter;

import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.Filter;

import java.util.List;

/**
 *  Class that implements a filter based on which students offered first
 */
public class FirstComeFilter implements Filter {
	/**
	 * As of now, a first come filter doesnt change the given list as whatever TradeOffer came first is positioned at index 0. Method may be changed later
	 * @param offers list of offers
	 * @return unchanged list
	 */
	@Override
	public List<TradeOffer> filter(List<TradeOffer> offers) {
		return offers;
	}
}
