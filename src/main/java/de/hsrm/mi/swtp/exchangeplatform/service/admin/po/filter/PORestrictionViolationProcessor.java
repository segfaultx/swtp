package de.hsrm.mi.swtp.exchangeplatform.service.admin.po.filter;

import de.hsrm.mi.swtp.exchangeplatform.model.admin.po.ChangedRestriction;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import de.hsrm.mi.swtp.exchangeplatform.model.data.PORestriction;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.RestrictionType;
import de.hsrm.mi.swtp.exchangeplatform.repository.ModuleRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.UserRepository;
import de.hsrm.mi.swtp.exchangeplatform.service.admin.po.POUpdateService;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.ModuleService;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class PORestrictionViolationProcessor implements Runnable {
	
	UserService userService;
	ModuleService moduleService;
	POUpdateService poUpdateService;
	UserRepository userRepository;
	ModuleRepository moduleRepository;
	PORestrictionViolationService poRestrictionViolationService;
	
	private void startProcessing() {
		List<ChangedRestriction> changedRestrictions = poUpdateService.getAllChangedPOs();
		if(changedRestrictions.size() > 0) log.info("...STARTED PROCESSING");
		for(ChangedRestriction changedRestriction : changedRestrictions) {
			final List<User> students = userService.getAllByPO(changedRestriction.getUpdatedPO());
			if(changedRestriction.getChangedRestrictions().contains(RestrictionType.CREDIT_POINTS)) {
				filterByCP(changedRestriction.getUpdatedPO().getRestriction().getByCP(), students);
			}
			if(changedRestriction.getChangedRestrictions().contains(RestrictionType.MINIMUM_SEMESTER)) {
				filterBySemester(changedRestriction.getUpdatedPO().getRestriction().getBySemester(), students);
			}
			if(changedRestriction.getChangedRestrictions().contains(RestrictionType.PROGRESSIVE_REGULATION)) {
				log.info(" // RestrictionType.PROGRESSIVE_REGULATION");
			}
		}
		poRestrictionViolationService.notifyUsersWithViolations();
		poUpdateService.flush();
	}
	
	public void filterByCP(final PORestriction.PORestrictionByCP restriction, final List<User> students) {
		if(!restriction.getIsActive()) return;
		log.info(" // RestrictionType.CREDIT_POINTS");
		final Long maxCp = restriction.getMaxCP();
		for(User student : students) {
			log.info(" // FILTERING: " + student.getAuthenticationInformation().getUsername());
			Long userCp = userService.getUserTotalCPSelected(student);
			
			if(userCp >= maxCp) {
				log.info(student.getAuthenticationInformation().getUsername() + " => VIOLATION DETECTED:filterByCP ======== ");
				log.info(student.getAuthenticationInformation().getUsername() + " => // TOO MANY CP - more than " + maxCp);
				poRestrictionViolationService.addViolation(student, RestrictionType.CREDIT_POINTS, userCp);
			} else log.info(" // CAN HAVE MORE - has " + userCp);
		}
	}
	
	public void filterBySemester(final PORestriction.PORestrictionBySemester restriction, final List<User> students) {
		if(!restriction.getIsActive()) return;
		log.info(" // RestrictionType.MINIMUM_SEMESTER");
		final Long minSemester = restriction.getMinSemesters();
		final List<Module> allModulesTillMinSemester = moduleRepository.findModulesBySemesterIsLessThanEqual(minSemester);
		final List<Long> exptctedModulesIds = allModulesTillMinSemester.stream().map(Module::getId).collect(Collectors.toList());
		
		for(User student : students) {
			log.info(student.getAuthenticationInformation().getUsername() + " => // FILTERING: " + student.getAuthenticationInformation().getUsername());
			
			final List<Module> userModules = moduleService.getAllModulesByStudent(student);
			final List<Module> modulesOccupiedAboveMinSemester = userModules.stream()
																			.filter(module -> module.getSemester() > minSemester)
																			.collect(Collectors.toList());
			
			if(modulesOccupiedAboveMinSemester.size() < 1) {
				log.info(student.getAuthenticationInformation()
								.getUsername() + " => O.K.::SEMESTER CHECK; MAX SEMESTER OCCUPIED SMALLER THAN MINSEMESTER=" + minSemester);
				// if the student hasn't occupied any modules above minSemester
				// there won't be any Violation against the minSemester
				continue;
			}
			if(allModulesTillMinSemester.size() > student.getCompletedModules().size()) {
				// if the amount of completed modules is the same as the amount of modules which have to be completed
				// check whether the student completed every module from semester 1 till minSemester
				final List<Module> matchingModules = student.getCompletedModules()
															.stream()
															.filter(completed -> exptctedModulesIds.contains(completed.getId()))
															.collect(Collectors.toList());
				if(matchingModules.size() >= exptctedModulesIds.size()) {
					// if at least all expected modules have been completed
					// there is not violation
					continue;
				}
			}
			log.info(student.getAuthenticationInformation().getUsername() + " => VIOLATION DETECTED:filterBySemester ======== ");
			poRestrictionViolationService.addViolation(student, RestrictionType.MINIMUM_SEMESTER, minSemester);
			
		}
	}
	
	@Override
	public void run() {
		this.startProcessing();
	}
}
