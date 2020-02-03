package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.UserIsAlreadyAttendeeException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.LeaveModuleSuccessfulMessage;
import de.hsrm.mi.swtp.exchangeplatform.messaging.sender.PersonalMessageSender;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.repository.ModuleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A service class for manipulating module data
 */
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ModuleService {
	
	ModuleRepository repository;
	ModuleLookupService moduleLookupService;
	TimeslotService timeslotService;
	PersonalMessageSender sender;
	TradeOfferService tradeOfferService;
	
	/**
	 * Returns a list with modules.
	 * @param ids a list of Module.ids which are requested.
	 * @return a list with modules by their ids from the given argument.
	 */
	public List<Module> getAllByIds(List<Long> ids) {
		return repository.findByIds(ids);
	}
	
	public Optional<Module> getById(Long moduleId) {
		return repository.findById(moduleId);
	}
	
	public void addAttendeeToModule(Module module, User student) {
		// check if student is an attendee
		if(module.getAttendees().contains(student)) {
			log.info(String.format("FAIL: Student %s is already an attendee", student.getStudentNumber()));
			throw new UserIsAlreadyAttendeeException(student);
		}
		
		module.getAttendees().add(student);
		save(module);
		log.info(String.format("SUCCESS: Student %s added to appointment %s", student.getStudentNumber(), module.getId()));
	}
	
	public void removeStudentFromModule(Module module, User student) throws NotFoundException {
		// check all timeslots of student and remove those which match with module
		List<Timeslot> allTimeSlots = new ArrayList<>(module.getTimeslots());
		for(Timeslot timeslot : allTimeSlots) {
			if(timeslot.getAttendees().contains(student)) {
				timeslotService.removeAttendeeFromTimeslot(timeslot, student);
			}
		}
		
		module.getAttendees().remove(student);
		
		// Delete all active TradeOffers for Module that User has left
		List<TradeOffer> tradeOffersOfStudent = tradeOfferService.getAllTradeoffersForStudent(student);
		for(TradeOffer tradeOffer: tradeOffersOfStudent) {
			if(tradeOffer.getOffer().getModule().getId().equals(module.getId())) {
				tradeOfferService.deleteTradeOffer(tradeOffer);
			}
		}
		
		this.save(module);
		
		sender.send(student, LeaveModuleSuccessfulMessage.builder()
														 .module(module)
														 .time(LocalTime.now())
														 .build());
	}
	
	public Module save(Module module) {
		log.info(String.format("SUCCESS: Module %s created", module));
		return repository.save(module);
	}
	
	public void delete(Module module) throws IllegalArgumentException {
		repository.delete(module);
		log.info(String.format("SUCCESS: Module %s deleted", module));
	}
	
	/**
	 * Method to lookup potential modules for {@link de.hsrm.mi.swtp.exchangeplatform.model.data.User} student
	 * @param user username of student
	 * @return list of timeslots of potential modules
	 */
	public List<Timeslot> lookUpAvailableModulesForStudent(User user) {
		log.info(String.format("Looking up modules for Student: %s", user.getAuthenticationInformation().getUsername()));
		
		return moduleLookupService.lookUpTimeslotsForStudent(user);
	}
	
	public List<Module> getAllModulesByStudent(final User student) {
		return student.getTimeslots()
					  .stream()
					  .map(Timeslot::getModule)
					  .distinct()
					  .collect(Collectors.toList());
	}
	
}
