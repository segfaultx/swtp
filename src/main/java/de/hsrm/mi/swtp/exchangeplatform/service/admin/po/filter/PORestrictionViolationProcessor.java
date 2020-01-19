package de.hsrm.mi.swtp.exchangeplatform.service.admin.po.filter;

import de.hsrm.mi.swtp.exchangeplatform.model.admin.po.ChangedRestriction;
import de.hsrm.mi.swtp.exchangeplatform.model.data.PO;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.service.admin.po.POUpdateService;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class PORestrictionViolationProcessor {

	UserService userService;
	POUpdateService poUpdateService;

	public void startPorcessing() {
		List<ChangedRestriction> changedRestrictions = poUpdateService.getAllChangedPOs();
		if(changedRestrictions.size() > 0) log.info("...STARTED PROCESSING");
		for(ChangedRestriction changedRestriction : changedRestrictions) {
			filterByCP(changedRestriction.getUpdatedPO());
		}
	}

	public List<User> filterByCP(final PO po) {
		final List<User> students = userService.getAllByPO(po);
		final ArrayList<User> filtered = new ArrayList<>();

		for(User student : students) {
			log.info(" // FILTERING: " + student.getAuthenticationInformation().getUsername());
			Long maxCp = po.getRestriction().getByCP().getMaxCP();
			Long sumCp = 0L;
			for(Timeslot timeslot : student.getTimeslots()) {
				sumCp += 5L;
			}
			if(sumCp >= maxCp) log.info(" // TOO MANY CP - more than " + maxCp);
			else log.info(" // CAN HAVE MORE - has " + sumCp);
		}

		return null;
	}

}
