package de.hsrm.mi.swtp.exchangeplatform.service.admin.po.filter;

import de.hsrm.mi.swtp.exchangeplatform.messaging.message.admin.po.POChangeMessage;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.admin.po.violation.CPViolationMessage;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.admin.po.violation.DualPOViolationMessage;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.admin.po.violation.ProgressiveRegulationViolationMessage;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.admin.po.violation.SemesterViolationMessage;
import de.hsrm.mi.swtp.exchangeplatform.messaging.sender.POMessageSender;
import de.hsrm.mi.swtp.exchangeplatform.model.admin.po.ChangedRestriction;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import de.hsrm.mi.swtp.exchangeplatform.model.data.*;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.DayOfWeek;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.RestrictionType;
import de.hsrm.mi.swtp.exchangeplatform.repository.ModuleRepository;
import de.hsrm.mi.swtp.exchangeplatform.service.admin.po.POUpdateService;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.ModuleService;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// TODO: need to make PORestriction processing more modular and accessible from other processors
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class PORestrictionViolationProcessor implements Runnable {
	
	UserService userService;
	ModuleService moduleService;
	POUpdateService poUpdateService;
	ModuleRepository moduleRepository;
	PORestrictionViolationService poRestrictionViolationService;
	POMessageSender poMessageSender;
	
	public void startProcessing() {
		List<ChangedRestriction> changedRestrictions = poUpdateService.getAllChangedPOs();
		log.info("┌ STARTED PROCESSING VIOLATIONS OF CHANGED PO-RESTRICTIONS: " + changedRestrictions);
		int filterActionsTakenTotal = 0;
		for(ChangedRestriction changedRestriction : changedRestrictions) {
			poMessageSender.send(changedRestriction.getUpdatedPO(), POChangeMessage.builder()
													.po(changedRestriction.getUpdatedPO())
													.build());
			
			int filterActionsTaken = 0;
			log.info("├┬ STARTED PROCESSING OF VIOLATIONS");
			final List<User> students = userService.getAllByPO(changedRestriction.getUpdatedPO());
			if(changedRestriction.getChangedRestrictions().contains(RestrictionType.CREDIT_POINTS)) {
				filterByCP(changedRestriction.getUpdatedPO().getRestriction().getByCP(), students);
				filterActionsTaken++;
			}
			if(changedRestriction.getChangedRestrictions().contains(RestrictionType.MINIMUM_SEMESTER)) {
				filterBySemester(changedRestriction.getUpdatedPO().getRestriction().getBySemester(), students);
				filterActionsTaken++;
			}
			if(changedRestriction.getChangedRestrictions().contains(RestrictionType.PROGRESSIVE_REGULATION)) {
				filterByProgressiveRegulation(changedRestriction.getUpdatedPO(), students);
				filterActionsTaken++;
			}
			if(changedRestriction.getChangedRestrictions().contains(RestrictionType.DUAL)) {
				filterByDual(changedRestriction.getUpdatedPO(), students);
				filterActionsTaken++;
			}
			log.info("│├ STEPS TAKEN: " + filterActionsTaken);
			log.info("│└ STARTED PROCESSING VIOLATIONS OF CHANGED PO-RESTRICTIONS: " + changedRestrictions);
			filterActionsTakenTotal += filterActionsTaken;
		}
		log.info("└ ...FINISHED PROCESSING OF VIOLATIONS; FILTER STEPS TAKEN IN TOTAL=" + filterActionsTakenTotal);
		poRestrictionViolationService.notifyUsersWithViolations();
		poUpdateService.flush();
	}
	
	public void filterByCP(final PORestriction.PORestrictionByCP restriction, final List<User> students) {
		if(!restriction.getIsActive()) return;
		log.info("├┬─→ // RestrictionType.CREDIT_POINTS");
		final Long maxCp = restriction.getMaxCP();
		for(User student : students) {
			log.info("│├┬→ FILTERING: " + student.getAuthenticationInformation().getUsername());
			Long userCp = userService.getUserTotalCPSelected(student);
			
			if(userCp <= maxCp) continue;
			
			final CPViolationMessage message;
			message = CPViolationMessage.builder()
										.maxCPByPO(maxCp)
										.userCP(userCp)
										.build();
			poRestrictionViolationService.addViolation(student, RestrictionType.CREDIT_POINTS, message);
			log.info("││└→ VIOLATION DETECTED:CREDIT_POINTS " + student.getAuthenticationInformation().getUsername());
		}
		log.info("│└─→ // RestrictionType.CREDIT_POINTS END");
	}
	
	public void filterBySemester(final PORestriction.PORestrictionBySemester restriction, final List<User> students) {
		if(!restriction.getIsActive()) return;
		log.info("├┬─→ // RestrictionType.MINIMUM_SEMESTER");
		final Long minSemester = restriction.getMinSemesters();
		final List<Module> allModulesTillMinSemester = moduleRepository.findModulesBySemesterIsLessThanEqual(minSemester);
		final List<Long> exptctedModulesIds = allModulesTillMinSemester.stream().map(Module::getId).collect(Collectors.toList());
		
		for(User student : students) {
			log.info("│├┬→ FILTERING: " + student.getAuthenticationInformation().getUsername());
			
			final List<Module> userModules = moduleService.getAllModulesByStudent(student);
			final List<Long> userModulesIds = userModules.stream()
														 .map(Module::getId)
														 .collect(Collectors.toList());
			final List<Module> modulesOccupiedAboveMinSemester = userModules.stream()
																			.filter(module -> module.getSemester() > minSemester)
																			.collect(Collectors.toList());
			
			if(modulesOccupiedAboveMinSemester.isEmpty()) {
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
			poRestrictionViolationService.addViolation(student,
													   RestrictionType.MINIMUM_SEMESTER,
													   message);
			log.info("││└→ VIOLATION DETECTED:MINIMUM_SEMESTER " + student.getAuthenticationInformation().getUsername());
		}
		log.info("│└─→ // RestrictionType.MINIMUM_SEMESTER END");
	}
	
	public void filterByProgressiveRegulation(final PO po, final List<User> students) {
		final PORestriction.PORestrictionByProgressiveRegulation restriction = po.getRestriction().getByProgressiveRegulation();
		if(!restriction.getIsActive()) return;
		log.info("├┬─→ // RestrictionType.PROGRESSIVE_REGULATION");
		final Long poSemesterCount = po.getSemesterCount();
		final Long MIN_PROGRESSIVE_SEMESTER = poSemesterCount.intValue() >= 6 ? 4L : 2;
		
		for(User student : students) {
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
				continue;
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
			poRestrictionViolationService.addViolation(student,
													   RestrictionType.PROGRESSIVE_REGULATION,
													   message);
			log.info("││└→ VIOLATION DETECTED:PROGRESSIVE_REGULATION " + student.getAuthenticationInformation().getUsername());
		}
		log.info("│└─→ // RestrictionType.PROGRESSIVE_REGULATION END");
	}
	
	public void filterByDual(final PO po, final List<User> students) {
		PORestriction.DualPO restriction = po.getRestriction().getDualPO();
		if(!restriction.getIsActive()) return;
		log.info("├┬─→ // RestrictionType.DUAL");
		
		for(User student : students) {
			log.info("│├┬→ FILTERING: " + student.getAuthenticationInformation().getUsername());
			
			final Long currentSemester = student.getCurrentSemester();
			
			// if the student has a semester count higher than the semester count given from po
			// then there is no checking needed; student has to manage free days on his/her own
			// // eqivalent to a OutOfBoundsException
			if(currentSemester > po.getSemesterCount()) continue;
			final DayOfWeek freeDay = restriction.getFreeDayBySemester(currentSemester);
			final List<Timeslot> occupiedOnFreeDay = student.getTimeslots()
															.stream()
															.filter(timeslot -> timeslot.getDay().equals(freeDay))
															.collect(Collectors.toList());
			
			// if there aren't any timeslots occupied on semester-bound free day then there is no violation
			if(occupiedOnFreeDay.isEmpty()) continue;
			
			final DualPOViolationMessage message;
			message = DualPOViolationMessage.builder()
											.blockedDay(freeDay)
											.timeslotsNotAllowed(occupiedOnFreeDay.stream()
																				  .map(Timeslot::getId)
																				  .collect(Collectors.toList()))
											.build();
			poRestrictionViolationService.addViolation(student,
													   RestrictionType.DUAL,
													   message);
			log.info("││└→ VIOLATION DETECTED:DUAL: " + student.getAuthenticationInformation().getUsername());
		}
		log.info("│└─→ // RestrictionType.DUAL END");
	}
	
	@Override
	public void run() {
		log.info("\n>>>>>> STARTED EXTERNAL THREAD");
		this.startProcessing();
		log.info(">>>>>> ENDED EXTERNAL THREAD");
	}
}
