package de.hsrm.mi.swtp.exchangeplatform.service.admin.po.filter;

import de.hsrm.mi.swtp.exchangeplatform.messaging.message.admin.po.violation.CPViolationMessage;
import de.hsrm.mi.swtp.exchangeplatform.model.data.PO;
import de.hsrm.mi.swtp.exchangeplatform.model.data.PORestriction;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.RestrictionType;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.UserService;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class POFilterCP extends AbstractPOFilter {
	
	UserService userService;
	
	@Builder
	public POFilterCP(UserService userService) {
		super(RestrictionType.CREDIT_POINTS);
		this.userService = userService;
	}
	
	@Override
	public PORestrictionFilterResult filter(PO po, PORestrictionFilterResult result) {
		final PORestriction.PORestrictionByCP restriction = po.getRestriction().getByCP();
		final User student = result.getStudent();
		
		log.info("├┬─→ // RestrictionType.CREDIT_POINTS");
		log.info("│├┬→ FILTERING: " + student.getAuthenticationInformation().getUsername());
		
		final Long maxCp = restriction.getMaxCP();
		Long userCp = userService.getUserTotalCPSelected(student);
		
		if(userCp <= maxCp) return result;
		
		log.info("││└→ VIOLATION DETECTED:CREDIT_POINTS " + student.getAuthenticationInformation().getUsername());
		log.info("│└─→ // RestrictionType.CREDIT_POINTS END");
		
		final CPViolationMessage message;
		message = CPViolationMessage.builder()
									.maxCPByPO(maxCp)
									.userCP(userCp)
									.build();
		
		return result.extend(RestrictionType.CREDIT_POINTS, message);
	}
}
