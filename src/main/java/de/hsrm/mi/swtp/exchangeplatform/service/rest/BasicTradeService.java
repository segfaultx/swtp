package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.TypeOfUsers;
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
	
	TimeslotService timeslotService;
	
	@Override
	@Transactional
	public boolean doTrade(User student1, User student2, Timeslot timeslot1, Timeslot timeslot2) throws NotFoundException {
		
		if(!student1.getUserType().getType().equals(TypeOfUsers.STUDENT) ||
				!student2.getUserType().getType().equals(TypeOfUsers.STUDENT)) {
			log.info("Trade failed: One or both Users are not Students");
			return false;
		}
		
		timeslotService.addAttendeeToTimeslot(timeslot2.getId(), student1);
		timeslotService.removeAttendeeFromTimeslot(timeslot1.getId(), student1);
		
		timeslotService.addAttendeeToTimeslot(timeslot1.getId(), student2);
		timeslotService.removeAttendeeFromTimeslot(timeslot2.getId(), student2);
		
		// send message to user's personal queue telling that the trade was successful
		/*personalMessageSender.send(acceptedTrade.getOfferer(), TradeOfferSuccessfulMessage.builder()
																					   .tradeOfferId(acceptedTrade.getId())
																					   .build());
		 */
		return true;
	}
}
