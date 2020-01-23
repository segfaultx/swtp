package de.hsrm.mi.swtp.exchangeplatform.service.filter.utils;

import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.Filter;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.TradeFilter.CapacityFilter;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.TradeFilter.CollisionFilter;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.TradeFilter.NoOfferFilter;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.TradeFilter.OfferFilter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterUtils {
	
	private static Map<String, Class<? extends Filter>> map;
	
	private static FilterUtils instance;
	
	private FilterUtils() {
		map = new HashMap<>();
		map.put("CapacityFilter", CapacityFilter.class);
		map.put("CollisionFilter", CollisionFilter.class);
		map.put("NoOfferFilter", NoOfferFilter.class);
		map.put("OfferFilter", OfferFilter.class);
	}
	
	public static FilterUtils getInstance() {
		if(FilterUtils.instance == null) {
			FilterUtils.instance = new FilterUtils();
		}
		return FilterUtils.instance;
	}
	
	public List<TradeOffer> getFilteredTradeOffers(List<TradeOffer> tradeOffers) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		for(Map.Entry<String, Class<? extends Filter>> entry : map.entrySet()) {
			Method method = map.get(entry.getKey()).getMethod("doFilter");
			tradeOffers = (List<TradeOffer>) method.invoke(tradeOffers);
		}
		return tradeOffers;
	}
	
}