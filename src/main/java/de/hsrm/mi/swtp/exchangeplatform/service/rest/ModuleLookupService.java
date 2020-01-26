package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TimeTable;
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

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ModuleLookupService {
	
	ModuleRepository moduleRepository;
	TimeslotService timeslotService;
	
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
		var allModules = moduleRepository.findAllByPoAndIsActive(usr.getPo(), true);
		List<Module> potentialModules = new ArrayList<>();
		// add modules to list if user can allocate
		var restriction = usr.getPo().getRestriction();
		allModules.forEach(mod -> {if (!allModulesOfStudent.contains(mod)
				&& restriction.canAllocateModule(usr, mod))
			potentialModules.add(mod);});
		//TODO: add active flag to module?
		var remainingModules = potentialModules
				.stream()
				.filter(module -> !module.getTimeslots().isEmpty())
				.collect(toList());
		List<Timeslot> out = new ArrayList<>();
		
		for(Module module : remainingModules){
			for(Timeslot ts : module.getTimeslots()){
				if((ts.getTimeSlotType() != TypeOfTimeslots.VORLESUNG) && timeslotService.hasCollisions(ts, usr.getTimeslots())){
					remainingModules.remove(module);
					break;
				}
			}
		}
		
		// just get timeslots of type VORLESUNG
		remainingModules.forEach(module -> out
				.add(module
				.getTimeslots()
				.stream()
				.filter(ts -> ts.getTimeSlotType() == TypeOfTimeslots.VORLESUNG)
								.findFirst().orElseThrow()));
		return out;
	}
}
