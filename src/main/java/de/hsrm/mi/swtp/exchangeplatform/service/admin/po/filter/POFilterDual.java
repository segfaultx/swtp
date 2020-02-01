package de.hsrm.mi.swtp.exchangeplatform.service.admin.po.filter;

import de.hsrm.mi.swtp.exchangeplatform.messaging.message.admin.po.violation.DualPOViolationMessage;
import de.hsrm.mi.swtp.exchangeplatform.model.data.PO;
import de.hsrm.mi.swtp.exchangeplatform.model.data.PORestriction;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.DayOfWeek;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.RestrictionType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class POFilterDual extends AbstractPOFilter {
	
	@Builder
	public POFilterDual() {
		super(RestrictionType.DUAL);
	}
	
	@Override
	public PORestrictionFilterResult filter(PO po, PORestrictionFilterResult result) {
		final PORestriction.DualPO restriction = po.getRestriction().getDualPO();
		final User student = result.getStudent();
		
		log.info("├┬─→ // RestrictionType.DUAL");
		log.info("│├┬→ FILTERING: " + student.getAuthenticationInformation().getUsername());
		
		final Long currentSemester = student.getCurrentSemester();
		
		// if the student has a semester count higher than the semester count given from po
		// then there is no checking needed; student has to manage free days on his/her own
		// // eqivalent to a OutOfBoundsException
		if(currentSemester > po.getSemesterCount()) return result;
		final DayOfWeek freeDay = restriction.getFreeDayBySemester(currentSemester);
		final List<Timeslot> occupiedOnFreeDay = student.getTimeslots()
														.stream()
														.filter(timeslot -> timeslot.getDay().equals(freeDay))
														.collect(Collectors.toList());
		
		// if there aren't any timeslots occupied on semester-bound free day then there is no violation
		if(occupiedOnFreeDay.isEmpty()) return result;
		
		log.info("││└→ VIOLATION DETECTED:DUAL: " + student.getAuthenticationInformation().getUsername());
		log.info("│└─→ // RestrictionType.DUAL END");
		
		final DualPOViolationMessage message;
		message = DualPOViolationMessage.builder()
										.blockedDay(freeDay)
										.timeslotsNotAllowed(occupiedOnFreeDay.stream()
																			  .map(Timeslot::getId)
																			  .collect(Collectors.toList()))
										.build();
		
		return result.extend(RestrictionType.DUAL, message);
	}
}
