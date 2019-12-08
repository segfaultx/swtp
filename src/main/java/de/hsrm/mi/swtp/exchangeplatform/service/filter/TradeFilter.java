package de.hsrm.mi.swtp.exchangeplatform.service.filter;


import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import de.hsrm.mi.swtp.exchangeplatform.repository.TimeslotRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.TradeOfferRepository;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class TradeFilter {
	
	@Autowired
	private StudentService studentService;
	
	@Autowired
	private TimeslotRepository timeSlotRepository;
	
	@Autowired
	private TradeOfferRepository tradeOfferRepository;
	
	
	/**
	 * Finds all timeslots of a given module and categorizes into 4 groups.
	 * Group 1: Timeslot collides with any other timeslot of student
	 * Group 2: Timeslot capacity hasnt been matched allowing instant subscription
	 * Group 3: Timeslot capacity has been matched, but another student offers to trade
	 * Group 4: No trade offers available, student is placed in a queue (aka what's left)
	 *
	 * @param tradeMap Timeslot that the user wishes to trade
	 * @param filters  Student looking for a diffrent timeslot
	 *
	 * @return map of list of timeslots for each category
	 */
	public Map<Timeslot, Map<String, List<TradeOffer>>> applyFilter(Map<Timeslot, List<TradeOffer>> tradeMap, List<Filter> filters) {
		Map<Timeslot, Map<String, List<TradeOffer>>> trades = new HashMap<>();
		Map<String, List<TradeOffer>> filteredMap = new HashMap<>();
		
		tradeMap.forEach((timeslot, tradeoffers) -> {
			for(Filter subFilter : filters) {
				filteredMap.put(subFilter.getClass().getName(), subFilter.filter(tradeoffers));
			}
			trades.put(timeslot, filteredMap);
		});
		
		return trades;
	}
}
