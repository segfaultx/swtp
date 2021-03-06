package de.hsrm.mi.swtp.exchangeplatform.service.admin.po.filter;

import de.hsrm.mi.swtp.exchangeplatform.messaging.message.admin.po.POChangeMessage;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.admin.po.violation.UserOccupancyViolationMessage;
import de.hsrm.mi.swtp.exchangeplatform.messaging.sender.POMessageSender;
import de.hsrm.mi.swtp.exchangeplatform.model.admin.po.ChangedRestriction;
import de.hsrm.mi.swtp.exchangeplatform.model.data.PO;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.RestrictionType;
import de.hsrm.mi.swtp.exchangeplatform.service.admin.po.POUpdateService;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * A service class which will be started after a {@link PO} and its {@link de.hsrm.mi.swtp.exchangeplatform.model.data.PORestriction} have been changed and activated.
 * <p>
 * This class will iterate through all {@link PO#students} and check whether any of the students' occupancy violates the new {@link de.hsrm.mi.swtp.exchangeplatform.model.data.PORestriction}. IF that was the case the affected student will be {@link PORestrictionViolationService#notifyUsersWithViolations() notified}.
 */
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PORestrictionProcessor implements Runnable {
	
	UserService userService;
	POUpdateService poUpdateService;
	POMessageSender poMessageSender;
	PORestrictionViolationService poRestrictionViolationService;
	
	// PO-Restriction FILTERS
	POFilterCP poFilterCP;
	POFilterDual poFilterDual;
	POFilterSemester poFilterSemester;
	POFilterProgressiveRegulation poFilterProgressiveRegulation;
	
	/**
	 * Processes all changed POs and their {@link PO#students} by first creating a {@link POFilterChain} and then applying all those filters to each students.
	 * After every student has been processed the will each receive a {@link UserOccupancyViolationMessage}.
	 */
	public void startProcessing() {
		List<ChangedRestriction> changedRestrictions = poUpdateService.getAllChangedPOs();
		log.info("??? STARTED PROCESSING VIOLATIONS OF CHANGED PO-RESTRICTIONS: " + changedRestrictions);
		int filterActionsTakenTotal = 0;
		for(ChangedRestriction changedRestriction : changedRestrictions) {
			final POFilterChain poFilterChain = POFilterChain.builder().build();
			int filterActionsTaken = 0;
			log.info("?????? STARTED PROCESSING OF VIOLATIONS");
			
			poMessageSender.send(changedRestriction.getUpdatedPO(), POChangeMessage.builder().po(changedRestriction.getUpdatedPO()).build());
			
			final List<User> students = userService.getAllByPO(changedRestriction.getUpdatedPO());
			final PO changedPO = changedRestriction.getUpdatedPO();
			
			for(User student : students) {
				if(changedRestriction.getChangedRestrictions().contains(RestrictionType.CREDIT_POINTS)) {
					poFilterChain.appendFilter(poFilterCP);
					filterActionsTaken++;
				}
				if(changedRestriction.getChangedRestrictions().contains(RestrictionType.MINIMUM_SEMESTER)) {
					poFilterChain.appendFilter(poFilterSemester);
					filterActionsTaken++;
				}
				if(changedRestriction.getChangedRestrictions().contains(RestrictionType.PROGRESSIVE_REGULATION)) {
					poFilterChain.appendFilter(poFilterProgressiveRegulation);
					filterActionsTaken++;
				}
				if(changedRestriction.getChangedRestrictions().contains(RestrictionType.DUAL)) {
					poFilterChain.appendFilter(poFilterDual);
					filterActionsTaken++;
				}
				
				poRestrictionViolationService.setViolations(poFilterChain.processAllForStudent(changedPO, student));
			}
			
			log.info("?????? STEPS TAKEN: " + filterActionsTaken);
			log.info("?????? STARTED PROCESSING VIOLATIONS OF CHANGED PO-RESTRICTIONS: " + changedRestrictions);
			filterActionsTakenTotal += filterActionsTaken;
		}
		log.info("??? ...FINISHED PROCESSING OF VIOLATIONS; FILTER STEPS TAKEN IN TOTAL=" + filterActionsTakenTotal);
		poRestrictionViolationService.notifyUsersWithViolations();
		poUpdateService.flush();
	}
	
	@Override
	public void run() {
		this.startProcessing();
	}
}
