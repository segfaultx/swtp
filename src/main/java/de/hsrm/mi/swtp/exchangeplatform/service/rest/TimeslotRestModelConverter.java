package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.rest_models.DayEnum;
import de.hsrm.mi.swtp.exchangeplatform.model.rest_models.PossibleTradesResponse;
import de.hsrm.mi.swtp.exchangeplatform.repository.TimeslotRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.TradeOfferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
@Service
@Slf4j
@RequiredArgsConstructor
public class TimeslotRestModelConverter implements RestModelConverter {
	
	TradeOfferService tradeOfferService;
	TradeOfferRepository tradeOfferRepository;
	TimeslotRepository timeslotRepository;
	
	@Override
	public boolean isResponsible(Object object) {
		return object instanceof Timeslot;
	}
	
	@Override
	public Object convertToRest(Object object) {
		Timeslot timeslot = (Timeslot) object;
		de.hsrm.mi.swtp.exchangeplatform.model.rest_models.Timeslot out = new de.hsrm.mi.swtp.exchangeplatform.model.rest_models.Timeslot();
		out.setDay(DayEnum.valueOf(timeslot.getDay()));
		out.setTimeStart(Timestamp.valueOf(timeslot.getTimeStart().toString()));
		out.setTimeEnd(Timestamp.valueOf(timeslot.getTimeEnd().toString()));
		PossibleTradesResponse possibleTradesResponse = new PossibleTradesResponse();
		var instantTrades = tradeOfferRepository.findAllBySeekAndInstantTrade(timeslot, true);
		instantTrades.forEach(tradeOffer -> possibleTradesResponse.addInstantItem(tradeOffer.getOffer().getId()));
		var regularTrades = tradeOfferRepository.findAllBySeekAndInstantTrade(timeslot, false);
		regularTrades.forEach(tradeOffer -> possibleTradesResponse.addTradesAvailableItem((tradeOffer.getOffer().getId())));
		var others = timeslotRepository.findAllByModule(timeslot.getModule());
		others.forEach(timeslt -> {
			if(!possibleTradesResponse.getInstant().contains(timeslt.getId()) && !possibleTradesResponse.getTradesAvailable().contains(timeslt.getId()))
				possibleTradesResponse.addRemainingItem(timeslt.getId());
		});
		out.setPossibleTrades(possibleTradesResponse);
		return out;
	}
}
