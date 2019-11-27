package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.TradeOfferNotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import de.hsrm.mi.swtp.exchangeplatform.repository.StudentRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.TimeslotRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.TradeOfferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class TradeOfferService {
    @Autowired
    private TradeOfferRepository tradeOfferRepository;

    @Autowired
    private TimeslotRepository timeSlotRepository;

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

    public TradeOffer createTradeOffer(long studentId, long offerId, long seekId) throws NotFoundException {
        TradeOffer tradeoffer = new TradeOffer();
        tradeoffer.setOfferer(studentRepository.findById(studentId).orElseThrow(() -> new NotFoundException(studentId)));
        tradeoffer.setOffer(timeSlotRepository.findById(offerId).orElseThrow(()-> new NotFoundException(offerId)));
        tradeoffer.setSeek(timeSlotRepository.findById(seekId).orElseThrow(() -> new NotFoundException(seekId)));
        return tradeOfferRepository.save(tradeoffer);
    }
}
