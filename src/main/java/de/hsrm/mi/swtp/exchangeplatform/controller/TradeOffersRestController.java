package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.TradeOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/tradeoffers")
public class TradeOffersRestController {
	
	@Autowired
	private TradeOfferService tradeOfferService;
	
	
	@GetMapping("/{module}/{timeslot}")
	public Map<String, List<Timeslot>> getTradeOffersForTimeSlot(@PathVariable("module") Module module, @PathVariable("timeslot") Timeslot timeslot) {
		return tradeOfferService.getTradeOffersForTimeSlot(timeslot, module);
	}
}
