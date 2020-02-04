package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.TypeOfUsers;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Basic Tradeservice implementation
 * @author amatus, Dennis S.
 */
@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BasicTradeService implements TradeService {
	
	TimeslotService timeslotService;
	
	/**
	 * Method for trading two Timeslots between to Students.
	 * @param student1 Student offering timeslot1
	 * @param student2 Student offering timeslot2
	 * @param timeslot1 Timeslot offered by student1
	 * @param timeslot2 Timeslot offered by student2
	 * @return true if trade was processed, false if not
	 */
	@Override
	public boolean doTrade(User student1, User student2, Timeslot timeslot1, Timeslot timeslot2) {
		
		if(!student1.getUserType().getType().equals(TypeOfUsers.STUDENT) ||
				!student2.getUserType().getType().equals(TypeOfUsers.STUDENT)) {
			log.info("Trade failed: One or both Users are not Students");
			return false;
		}
		
		try {
			
			timeslotService.removeAttendeeFromTimeslot(timeslot2, student1);
			timeslotService.removeAttendeeFromTimeslot(timeslot1, student2);
			
			timeslotService.addAttendeeToTimeslot(timeslot1, student1);
			timeslotService.addAttendeeToTimeslot(timeslot2, student2);
			
		} catch(Exception e) {
			log.info("Something went wrong");
			log.info("Trade not processed");
			return false;
		}
		return true;
	}
}
