package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
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

	UserService userService;
	TimeslotService timeslotService;
	TradeOfferService tradeOfferService;
	
	@Override
	@Transactional
	public boolean doTrade(long studentId, long offeredTimeSlotId, long wantedTimeSlotId) throws NotFoundException {
		User student1 = userService.getById(studentId)
				.orElseThrow(NotFoundException::new);
		
		timeslotService.addAttendeeToTimeslot(wantedTimeSlotId, student1);
		timeslotService.removeAttendeeFromTimeslot(offeredTimeSlotId, student1);
		
		TradeOffer tradeOffer = tradeOfferService.getById(offeredTimeSlotId);
		
		// TODO: Connect active filters and return false if trade did not succeed
		
		User student2 = tradeOffer.getOfferer();
		
		timeslotService.addAttendeeToTimeslot(offeredTimeSlotId, student2);
		timeslotService.removeAttendeeFromTimeslot(wantedTimeSlotId, student2);
		
		// send message to user's personal queue telling that the trade was successful
		/*personalMessageSender.send(acceptedTrade.getOfferer(), TradeOfferSuccessfulMessage.builder()
																					   .tradeOfferId(acceptedTrade.getId())
																					   .build());
		 */
		return true;
	}
}
