package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import de.hsrm.mi.swtp.exchangeplatform.repository.TimeslotRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.TradeOfferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TradeOfferService {
    @Autowired
    private TradeOfferRepository tradeOfferRepository;

    @Autowired
    private TimeslotRepository timeSlotRepository;


    public Map<String, List<Timeslot>> getTradeOffersForTimeSlot(Timeslot offer, Module module) {
        Map<String, List<Timeslot>> offersMap = new HashMap<>();
        var tradeoffers = tradeOfferRepository.findAllBySeek(offer);
        offersMap.put("Trade", tradeoffers.stream().filter(item -> item.getOfferer() != null
                && !item.isInstantTrade()).map(TradeOffer::getOffer).collect(Collectors.toList()));
        offersMap.put("Instant", tradeoffers.stream()
                .filter(TradeOffer::isInstantTrade)
                .map(TradeOffer::getOffer)
                .collect(Collectors.toList()));
        offersMap.put("Remaining", new ArrayList<>());
        for (Timeslot timeslot : timeSlotRepository.findAllByModule(module)) {
            if (!offersMap.get("Trade").contains(timeslot) && !offersMap.get("Instand").contains(timeslot)) {
                offersMap.get("Remaining").add(timeslot);
            }
        }
        return offersMap;
    }
}
