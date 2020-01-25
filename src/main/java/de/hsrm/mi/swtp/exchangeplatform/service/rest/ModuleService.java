package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.UserIsAlreadyAttendeeException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.model.rest.BatchModulesRequest;
import de.hsrm.mi.swtp.exchangeplatform.repository.ModuleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ModuleService {
	
	ModuleRepository repository;
	TimeslotService timeslotService;
	
	public List<Module> getAll() {
		return repository.findAll();
	}
	
	public List<Module> getAllById(BatchModulesRequest batchModulesRequest) {
		return repository.findDistinctByIdIsIn(batchModulesRequest.getModulesIDs());
	}
	
	public Optional<Module> getById(Long moduleId) {
		return repository.findById(moduleId);
	}
	
	public void addAttendeeToModule(Long moduleId, User student) throws NotFoundException {
		Module module = this.getById(moduleId).orElseThrow(NotFoundException::new);
		
		if(module.getAttendees().contains(student)) {
			log.info(String.format("FAIL: Student %s is already an attendee", student.getStudentNumber()));
			throw new UserIsAlreadyAttendeeException(student);
		}
		
		module.getAttendees().add(student);
		this.save(module);
		log.info(String.format("SUCCESS: Student %s added to appointment %s", student.getStudentNumber(), moduleId));
	}
	
	public void removeStudentFromModule(Long moduleId, User student) throws NotFoundException {
		Module module = this.getById(moduleId).orElseThrow(NotFoundException::new);
		
		List<Timeslot> allTimeSlots = new ArrayList<>(module.getTimeslots());
		for(Timeslot timeslot : allTimeSlots) {
			if(timeslot.getAttendees().contains(student)) {
				timeslotService.removeAttendeeFromTimeslot(timeslot, student);
				student.getTimeslots().remove(timeslot);
			}
		}
		
		module.getAttendees().remove(student);
		this.save(module);
	}
	
	public void save(Module module) {
		/*if(this.repository.existsById(module.getId())) {
			log.info(String.format("FAIL: Module %s not created", module));
			throw new NotCreatedException(module);
		} */
		repository.save(module);
		log.info(String.format("SUCCESS: Module %s created", module));
	}
	
	public void delete(Module module) throws IllegalArgumentException {
		repository.delete(module);
		log.info(String.format("SUCCESS: Module %s deleted", module));
	}
	
	public List<Module> getAllModulesByStudent(final User student) {
		return student.getTimeslots()
					  .stream()
					  .map(Timeslot::getModule)
					  .distinct()
					  .collect(Collectors.toList());
	}
	
}
