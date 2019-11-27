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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class TradeOfferService implements RestService<TradeOffer, Long> {
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

    public boolean tradeTimeslots(long requesterId, long tradeId) throws RuntimeException {
        log.info(String.format("Performing Trade for requester: %d, trade: %d", requesterId, tradeId));
        var requester = studentRepository.findById(requesterId).orElseThrow(() -> {
            log.info(String.format("Error fetching Student from repository with ID: %d", requesterId));
            throw new NotFoundException(requesterId);
        });
        var trade = tradeOfferRepository.findById(tradeId).orElseThrow(() -> {
            log.info(String.format("Error fetching Tradeoffer with ID: %d", tradeId));
            throw new NotFoundException(tradeId);
        });

        return true; //TODO real trade method
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
        log.info(String.format("Creating new Tradeoffer for Student: %d with offer/request: %d/%d", studentId, offerId, seekId));
        TradeOffer tradeoffer = new TradeOffer();
        tradeoffer.setOfferer(studentRepository.findById(studentId).orElseThrow(() -> {
            log.info(String.format("ERROR while creating Tradeoffer: StudentId not found: %d", studentId));
            throw new NotFoundException(studentId);
        }));
        tradeoffer.setOffer(timeSlotRepository.findById(offerId).orElseThrow(() -> {
            log.info(String.format("ERROR while creating Tradeoffer: offerId not found: %d", offerId));
            throw new NotFoundException(offerId);
        }));
        tradeoffer.setSeek(timeSlotRepository.findById(seekId).orElseThrow(() -> {
            log.info(String.format("ERROR while creating Tradeoffer: seekId not found: %d", seekId));
            throw new NotFoundException(seekId);
        }));
        log.info(String.format("Successfully created new Tradeoffer for Student: %d with offer/seek: %d/%d", studentId, offerId, seekId));
        return tradeOfferRepository.save(tradeoffer);
    }

    @Override
    public List<TradeOffer> getAll() {
        return null;
    }

    @Override
    public TradeOffer getById(Long aLong) throws NotFoundException {
        return tradeOfferRepository.findById(aLong).orElseThrow(() -> new NotFoundException(aLong));
    }

    @Override
    public void save(TradeOffer item) throws IllegalArgumentException {
        var dbItem = tradeOfferRepository.save(item);
        log.info(String.format("Successfully saved Tradeoffer with ID: %d", dbItem.getId()));
    }

    @Override
    public void delete(Long aLong) throws IllegalArgumentException {
        tradeOfferRepository.delete(tradeOfferRepository.findById(aLong).orElseThrow());
    }

    @Override
    public boolean update(Long aLong, TradeOffer update) throws IllegalArgumentException {
        log.info(String.format("Updating TradeOffer: %d", aLong));
        var dbItem = tradeOfferRepository.findById(aLong).orElseThrow(() -> {
            log.info(String.format("ERROR Updating TradeOffer: %d", aLong));
            throw new IllegalArgumentException();
        });
        BeanUtils.copyProperties(update, dbItem, "id");
        log.info(String.format("Successfully updated Tradeoffer: %d", aLong));
        return true;
    }
}
