package de.hsrm.mi.swtp.exchangeplatform.service.filter.utils;

import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import de.hsrm.mi.swtp.exchangeplatform.repository.CustomPythonFilterRepository;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.Filter;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.TradeFilter.CustomPythonFilter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Utils class for filtering TradeOffers
 *
 * @author Dennis Schad
 *
 */
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilterUtils {
	
	Map<String, Filter> map;
	
	List<Filter> allSystemFilters;
	
	CustomPythonFilterRepository customPythonFilterRepository;
	
	/**
	 * Constructor of FilterUtils
	 * @param allSystemFilters list of system filters
	 * @param customPythonFilterRepository repository for custom python filters
	 */
	@Autowired
	public FilterUtils(@NotNull List<Filter> allSystemFilters, @NotNull CustomPythonFilterRepository customPythonFilterRepository){
		this.allSystemFilters = allSystemFilters;
		this.customPythonFilterRepository = customPythonFilterRepository;
		map = new HashMap<>();
		allSystemFilters.forEach(fltr -> map.put(fltr.getFilterName(), fltr));
	}
	
	/**
	 * Method for filtering TradeOffers with all active filters
	 * @param tradeOffers list of TradeOffers that are to be filtered
	 * @return list of filtered TradeOffers
	 */
	public List<TradeOffer> getFilteredTradeOffers(List<TradeOffer> tradeOffers) {
		for(Filter entry : map.values()) {
			tradeOffers = entry.doFilter(tradeOffers);
		}
		return tradeOffers;
	}
	
	/**
	 * Method for getting a map with all active filters
	 * @return returns a map of all active filters
	 */
	public Map<String,Filter> getMap() {
		return map;
	}
	
	/**
	 * Fetches all Entries in activeFilters and returns them as List of Strings
	 * @return List of Strings with names of active Filters
	 */
	public List<String> getActiveFilterList() {
		return map.values()
				  .stream()
				  .map(Filter::getFilterName)
				  .collect(toList());
	}
	
	/**
	 * Method to check if a given filter exists
	 * @param nameOfFilter name of filter
	 * @return true if exists
	 */
	public boolean filterExists (String nameOfFilter){
		// check system filters for a match
		var systemFilterMatch = allSystemFilters.stream().anyMatch(filter -> filter.getFilterName().equals(nameOfFilter));
		// check python filters for a match
		var pythonFilterMatch = customPythonFilterRepository.findAll()
															.stream()
															.anyMatch(filter -> filter.getFilterName().equals(nameOfFilter));
		return systemFilterMatch || pythonFilterMatch;
	}
	
	/**
	 * Adds a new filter to the active filters
	 * @param key Name of the filter
	 * @param filter class of filter, this class should implement the Filter interface
	 */
	public void addFilter(String key, Filter filter) {
		map.put(key, filter);
	}
	
	/**
	 * Removes a filter from the map of active filters
	 * @param key name of the filter to be removed
	 */
	public void removeFilter(String key) {
		map.remove(key);
	}
	
	/**
	 * Gets class of Filter by given Name
	 * @param nameOfFilter name of the Filter class
	 * @return Class of Filter or null if not present
	 */
	
	public Filter getFilterByName(String nameOfFilter){
		// First check system filters, if name isnt to be found check all customPython filters
		return allSystemFilters.stream().filter(item -> item.getFilterName().equals(nameOfFilter))
				.findAny().orElseGet(() -> {
					var allCustomPythonFilters = customPythonFilterRepository.findAll();
					return allCustomPythonFilters.stream()
												 .filter(customFilter -> customFilter.getFilterName().equals(nameOfFilter))
							.findAny().orElse(null);
				});
	}
	
	/**
	 * Method to update filters of filterUtils
	 * @param activeFilters list containing the names of filters which should be active
	 */
	public void setActiveFilters(List<String> activeFilters){
		map.clear();
		activeFilters.forEach(filtername -> {
			var fltr = getFilterByName(filtername);
			map.put(fltr.getFilterName(), fltr);
		});
	}
	
	/**
	 * Method to get all available filters of the system (standard + custom)
	 * @return list containing the names of all available filters
	 */
	public List<String> getAllAvailableFilters(){
		// add all system filter names
		List<String> out = allSystemFilters.stream().map(Filter::getFilterName).collect(Collectors.toList());
		// add all custom python filter names
		out.addAll(customPythonFilterRepository.findAll().stream().map(Filter::getFilterName).collect(toList()));
		return out;
	}
	
	public CustomPythonFilter addPythonFilter(String filterName, String code){
		CustomPythonFilter toAdd = new CustomPythonFilter(filterName, code);
		return customPythonFilterRepository.save(toAdd);
	}
}