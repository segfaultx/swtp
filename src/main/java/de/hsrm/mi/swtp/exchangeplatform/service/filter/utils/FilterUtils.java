package de.hsrm.mi.swtp.exchangeplatform.service.filter.utils;

import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.Filter;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.TradeFilter.CapacityFilter;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.TradeFilter.CollisionFilter;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.TradeFilter.NoOfferFilter;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.TradeFilter.OfferFilter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utils class for filtering TradeOffers
 *
 * @author Dennis Schad
 *
 */
@Slf4j
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
	 * Fetches all Entries in activeFilters and returns them as List of Strings
	 * @return List of Strings with names of active Filters
	 */
	public List<String> getActiveFilterList() {
		List<String> activeFilters = new ArrayList<>();
		for(Map.Entry<String, Class<? extends Filter>> entry : map.entrySet()) {
			activeFilters.add(entry.getKey());
		}
		return activeFilters;
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
	
	public boolean filterExists(String nameOfFilter) {
		try {
			Class.forName(nameOfFilter);
			log.info("Class {}.class found in classpath", nameOfFilter);
			return true;
		} catch(ClassNotFoundException e) {
			log.info("Class {}.class not found in classpath", nameOfFilter);
			return false;
		}
	}
	
	/**
	 * Gets class of Filter by given Name
	 * @param nameOfFilter name of the Filter class
	 * @return Class of Filter or null if not present
	 * @throws ClassNotFoundException if class can not be found in Classpath
	 */
	public Class<? extends Filter> getFilterByName(String nameOfFilter) throws ClassNotFoundException {
		if(filterExists(nameOfFilter)) return (Class<? extends Filter>) Class.forName(nameOfFilter);
		return null;
	}
	
}