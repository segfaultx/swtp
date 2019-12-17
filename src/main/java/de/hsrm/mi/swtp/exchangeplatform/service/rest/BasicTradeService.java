package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.repository.StudentRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.TimeslotRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.TradeOfferRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BasicTradeService implements TradeService{

	StudentRepository studentRepository;
	TradeOfferRepository tradeOfferRepository;
	TimeslotRepository timeslotRepository;
	
	@Override
	@Transactional
	public boolean doTrade(long studentId, long offeredId, long wantedId) throws NotFoundException {
		var student = studentRepository.findById(studentId).orElseThrow();
		var offered = timeslotRepository.findById(offeredId).orElseThrow();
		var tradeOffers = tradeOfferRepository.findAllBySeek(offered);
		var acceptedTrade = tradeOffers.get(0);
		student.getTimeslots().remove(offered);
		student.getTimeslots().add(acceptedTrade.getOffer());
		var student2 = acceptedTrade.getOfferer();
		student2.getTimeslots().remove(acceptedTrade.getOffer());
		student2.getTimeslots().add(offered);
		studentRepository.save(student);
		studentRepository.save(student2);
		return true;
	}
}
