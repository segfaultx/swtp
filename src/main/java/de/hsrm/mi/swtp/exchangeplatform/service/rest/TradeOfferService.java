package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.TradeOfferNotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import de.hsrm.mi.swtp.exchangeplatform.repository.StudentRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.TimeSlotRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.TradeOfferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Null;
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
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    private StudentRepository studentRepository;


    public Map<Timeslot, List<TradeOffer>> getTradeOffersForTimeSlots(List<Timeslot> timeslots) {
        Map<Timeslot, List<TradeOffer>> timeslotTradeOffers = new HashMap<>();
        timeslots.forEach(timeslot -> timeslotTradeOffers
                .put(timeslot, tradeOfferRepository.findAllBySeek(timeslot)));
        return timeslotTradeOffers; //TODO: use filter module here
    }

    public Timeslot tradeTimeslots(TradeOffer offerer) {
        return null; //TODO real trade method
    }

    public List<TradeOffer> getTrades() {
        return tradeOfferRepository.findAll();
    }

    public boolean deleteTradeOffer(long studentId, long tradeId) throws RuntimeException {
        tradeOfferRepository.findById(tradeId).ifPresentOrElse(tradeOffer -> {
                    if (tradeOffer.getOfferer().getMatriculationNumber() != studentId)
                        throw new RuntimeException("not your tradeoffer");
                    tradeOfferRepository.delete(tradeOffer);
                },
                () -> {
                    throw new TradeOfferNotFoundException(tradeId);
                });
        return true;
    }

    public TradeOffer createTradeOffer(long studentId, long offerId, long seekId) {
        TradeOffer tradeoffer = new TradeOffer();
        tradeoffer.setOfferer(studentRepository.findById(studentId).get());
        tradeoffer.setOffer(timeSlotRepository.findById(offerId).get());
        tradeoffer.setSeek(timeSlotRepository.findById(seekId).get());
        return tradeOfferRepository.save(tradeoffer);
    }
}
