package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.TypeOfTimeslots;
import de.hsrm.mi.swtp.exchangeplatform.repository.ModuleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ModuleLookupService {
	
	ModuleRepository moduleRepository;
	
	/**
	 * Method to lookup potential Modules for {@link de.hsrm.mi.swtp.exchangeplatform.model.data.User} student
	 * @param usr user to lookup modules for
	 * @return list of timeslots for student
	 */
	public List<Timeslot> lookUpTimeslotsForStudent(User usr){

		List<Module> allModulesOfStudent = new ArrayList<>(); // List of completed + currently selected modules
		usr.getTimeslots().forEach(ts -> {
			if(!allModulesOfStudent.contains(ts.getModule()))
				allModulesOfStudent.add(ts.getModule());
		});
		allModulesOfStudent.addAll(usr.getCompletedModules());
		// TODO: Add PO to user to distinct module fetching
		var allModules = moduleRepository.findAllByPo(usr.getPo());
		List<Module> potentialModules = new ArrayList<>();
		// First collect ALL potential Modules, ignoring if they are active / selectable etc.
		var restriction = usr.getPo().getRestriction();
		allModules.forEach(mod -> {if (!allModulesOfStudent.contains(mod)) potentialModules.add(mod);});
		//TODO: add active flag to module?
		var remainingModules = potentialModules
				.stream()
				.filter(module -> !module.getTimeslots().isEmpty())
				.collect(toList());
		List<Timeslot> out = new ArrayList<>();
		
		// just get timeslots of type VORLESUNG
		//TODO: add term to modules, add restrictions to filter modules (e.g. Fortschrittsregelung MI)
		remainingModules.forEach(module -> out
				.add(module
				.getTimeslots()
				.stream()
				.filter(ts -> ts.getTimeSlotType() == TypeOfTimeslots.VORLESUNG)
								.findFirst().orElseThrow()));
		return out;
	}
}
