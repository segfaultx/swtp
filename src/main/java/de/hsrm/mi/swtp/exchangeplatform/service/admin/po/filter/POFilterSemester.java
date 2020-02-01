package de.hsrm.mi.swtp.exchangeplatform.service.admin.po.filter;

import de.hsrm.mi.swtp.exchangeplatform.messaging.message.admin.po.violation.SemesterViolationMessage;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import de.hsrm.mi.swtp.exchangeplatform.model.data.PO;
import de.hsrm.mi.swtp.exchangeplatform.model.data.PORestriction;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.RestrictionType;
import de.hsrm.mi.swtp.exchangeplatform.repository.ModuleRepository;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.ModuleService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class POFilterSemester extends AbstractPOFilter {
	
	ModuleService moduleService;
	ModuleRepository moduleRepository;
	
	public POFilterSemester(ModuleService moduleService, ModuleRepository moduleRepository) {
		super(RestrictionType.MINIMUM_SEMESTER);
		this.moduleService = moduleService;
		this.moduleRepository = moduleRepository;
	}
	
	@Override
	public PORestrictionFilterResult filter(PO po, PORestrictionFilterResult result) {
		final PORestriction.PORestrictionBySemester restriction = po.getRestriction().getBySemester();
		final User student = result.getStudent();
		
		log.info("├┬─→ // RestrictionType.MINIMUM_SEMESTER");
		log.info("│├┬→ FILTERING: " + student.getAuthenticationInformation().getUsername());
		
		final Long minSemester = restriction.getMinSemesters();
		final List<de.hsrm.mi.swtp.exchangeplatform.model.data.Module> allModulesTillMinSemester = moduleRepository.findModulesBySemesterIsLessThanEqual(minSemester);
		final List<Long> exptctedModulesIds = allModulesTillMinSemester.stream().map(de.hsrm.mi.swtp.exchangeplatform.model.data.Module::getId).collect(Collectors.toList());
		final List<de.hsrm.mi.swtp.exchangeplatform.model.data.Module> userModules = moduleService.getAllModulesByStudent(student);
		final List<Long> userModulesIds = userModules.stream()
													 .map(de.hsrm.mi.swtp.exchangeplatform.model.data.Module::getId)
													 .collect(Collectors.toList());
		final List<de.hsrm.mi.swtp.exchangeplatform.model.data.Module> modulesOccupiedAboveMinSemester = userModules.stream()
																													.filter(module -> module.getSemester() > minSemester)
																													.collect(Collectors.toList());
		
		if(modulesOccupiedAboveMinSemester.isEmpty()) {
			// if the student hasn't occupied any modules above minSemester
			// there won't be any Violation against the minSemester
			return result;
		}
		if(allModulesTillMinSemester.size() > student.getCompletedModules().size()) {
			// if the amount of completed modules is the same as the amount of modules which have to be completed
			// check whether the student completed every module from semester 1 till minSemester
			final List<de.hsrm.mi.swtp.exchangeplatform.model.data.Module> matchingModules = student.getCompletedModules()
																									.stream()
																									.filter(completed -> exptctedModulesIds.contains(completed.getId()))
																									.collect(Collectors.toList());
			if(matchingModules.size() >= exptctedModulesIds.size()) {
				// if at least all expected modules have been completed
				// there is not violation
				return result;
			}
		}
		
		log.info("││└→ VIOLATION DETECTED:MINIMUM_SEMESTER " + student.getAuthenticationInformation().getUsername());
		log.info("│└─→ // RestrictionType.MINIMUM_SEMESTER END");
		
		final SemesterViolationMessage message;
		message = SemesterViolationMessage.builder()
										  .poSemester(minSemester)
										  .missingModules(exptctedModulesIds
																  .stream()
																  .filter(id -> !userModulesIds.contains(id))
																  .collect(Collectors.toList()))
										  .modulesNotAllowed(modulesOccupiedAboveMinSemester.stream()
																							.map(Module::getId)
																							.collect(Collectors.toList()))
										  .build();
		
		return result.extend(RestrictionType.MINIMUM_SEMESTER, message);
	}
}
