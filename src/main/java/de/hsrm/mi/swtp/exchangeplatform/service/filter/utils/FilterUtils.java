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

/**
 * Utils class for filtering TradeOffers
 *
 * @author Dennis Schad
 *
 */
public class FilterUtils {
	
	private static Map<String, Class<? extends Filter>> map;
	
	private static FilterUtils instance;
	
	private FilterUtils() {
		// by default all Filters are active
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
	
	/**
	 * Method for filtering TradeOffers with all active filters
	 * @param tradeOffers list of TradeOffers that are to be filtered
	 * @return list of filtered TradeOffers
	 */
	public List<TradeOffer> getFilteredTradeOffers(List<TradeOffer> tradeOffers) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		for(Map.Entry<String, Class<? extends Filter>> entry : map.entrySet()) {
			Method method = map.get(entry.getKey()).getMethod("doFilter");
			tradeOffers = (List<TradeOffer>) method.invoke(tradeOffers);
		}
		return tradeOffers;
	}
	
	/**
	 * Method for getting a map with all active filters
	 * @return returns a map of all active filters
	 */
	public Map<String, Class<? extends Filter>> getMap() {
		return map;
	}
	
	/**
	 * Adds a new filter to the active filters
	 * @param key Name of the filter
	 * @param filter class of filter, this class should implement the Filter interface
	 */
	public void addFilter(String key, Class<? extends Filter> filter) {
		map.put(key, filter);
	}
	
	/**
	 * Removes a filter from the map of active filters
	 * @param key name of the filter to be removed
	 */
	public void removeFilter(String key) {
		map.remove(key);
	}
	
}