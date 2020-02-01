package de.hsrm.mi.swtp.exchangeplatform.service.admin.po.filter;

import de.hsrm.mi.swtp.exchangeplatform.messaging.message.admin.po.violation.ProgressiveRegulationViolationMessage;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import de.hsrm.mi.swtp.exchangeplatform.model.data.PO;
import de.hsrm.mi.swtp.exchangeplatform.model.data.PORestriction;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.RestrictionType;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.ModuleService;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class POFilterProgressiveRegulation extends AbstractPOFilter {
	
	UserService userService;
	ModuleService moduleService;
	
	public POFilterProgressiveRegulation(UserService userService, ModuleService moduleService) {
		super(RestrictionType.PROGRESSIVE_REGULATION);
		this.userService = userService;
		this.moduleService = moduleService;
	}
	
	@Override
	public PORestrictionFilterResult filter(PO po, PORestrictionFilterResult result) {
		final PORestriction.PORestrictionByProgressiveRegulation restriction = po.getRestriction().getByProgressiveRegulation();
		final User student = result.getStudent();
		
		log.info("├┬─→ // RestrictionType.PROGRESSIVE_REGULATION");
		final Long poSemesterCount = po.getSemesterCount();
		final Long MIN_PROGRESSIVE_SEMESTER = restriction.getSemesterSpan().getSemesterSpan();
		
		log.info("│├┬→ FILTERING: " + student.getAuthenticationInformation().getUsername());
		
		// an array which tells which semesters have been completed *FULLY*
		final Boolean[] userPassedSemesters = new Boolean[poSemesterCount.intValue()];
		for(int semesterIdx = 0; semesterIdx < poSemesterCount.intValue(); semesterIdx++) {
			userPassedSemesters[semesterIdx] = userService.userPassedSemester(student, (long) (semesterIdx + 1));
		}
		
		/** A list of modules a student has occupied which are dependent on previous semester */
		final List<Module> dependantOccupiedModules = moduleService.getAllModulesByStudent(student)
																   .stream()
																   .filter(module -> module.getSemester() >= MIN_PROGRESSIVE_SEMESTER)
																   .distinct()
																   .collect(Collectors.toList());
		
		// check whether any occupied modules are dependant on previous semesters
		if(dependantOccupiedModules.isEmpty()) {
			// if none found then there is no need to for validation
			return result;
		}
		
		// create a list with all modules ids of modules which the user is not allowed to take part in;
		// reason being: not fulfilling PROGRESSIVE_REGULATION by missing modules of previous semesters
		final ArrayList<Long> modulesNotAllowed = new ArrayList<>();
		for(Module occupied : dependantOccupiedModules) {
			final Long semester = occupied.getSemester();
			final int semesterIdx = Math.toIntExact(semester - MIN_PROGRESSIVE_SEMESTER);
			if(!userPassedSemesters[semesterIdx]) {
				modulesNotAllowed.add(occupied.getId());
			}
		}
		final ProgressiveRegulationViolationMessage message;
		message = ProgressiveRegulationViolationMessage.builder()
													   .modulesNotAllowed(modulesNotAllowed.stream()
																						   .sorted()
																						   .distinct()
																						   .collect(Collectors.toList()))
													   .build();
		
		log.info("││└→ VIOLATION DETECTED:PROGRESSIVE_REGULATION " + student.getAuthenticationInformation().getUsername());
		log.info("│└─→ // RestrictionType.PROGRESSIVE_REGULATION END");
		
		return result.extend(RestrictionType.PROGRESSIVE_REGULATION, message);
	}
}
