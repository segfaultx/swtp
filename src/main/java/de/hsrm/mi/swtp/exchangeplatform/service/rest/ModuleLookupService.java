package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.TypeOfTimeslots;
import de.hsrm.mi.swtp.exchangeplatform.repository.ModuleRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ModuleLookupService {
	
	UserRepository userRepository;
	ModuleRepository moduleRepository;
	
	/**
	 * Method to lookup potential Modules for {@link de.hsrm.mi.swtp.exchangeplatform.model.data.User} student
	 * @param studentname usrname of student
	 * @return list of timeslots for student
	 * @throws NotFoundException if username isnt known
	 */
	public List<Timeslot> lookUpTimeslotsForStudent(String studentname) throws NotFoundException {
		log.info(String.format("LOOKING UP USER WITH USERNAME: %s", studentname));
		var usr = userRepository.findByUsername(studentname).orElseThrow(()->{
			log.info(String.format("ERROR LOOKING UP STUDENT WITH USERNAME: %s -> NOT FOUND", studentname));
			return new NotFoundException();
		});
		List<Module> allModulesOfStudent = new ArrayList<>(); // List of completed + currently selected modules
		usr.getTimeslots().forEach(ts -> {
			if(!allModulesOfStudent.contains(ts.getModule()))
				allModulesOfStudent.add(ts.getModule());
		});
		allModulesOfStudent.addAll(usr.getCompletedModules());
		// TODO: Add PO to user to distinct module fetching
		var allModules = moduleRepository.findAllByPo(allModulesOfStudent.get(0).getPo()); // dirty hack
		List<Module> potentialModules = new ArrayList<>();
		// First collect ALL potential Modules, ignoring if they are active / selectable etc.
		allModules.forEach(mod -> {if (!allModulesOfStudent.contains(mod)) potentialModules.add(mod);});
		//TODO: add active flag to module?
		var remainingModules = potentialModules
				.stream()
				.filter(module -> !module.getTimeslots().isEmpty())
				.collect(Collectors.toList());
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
