package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notcreated.NotCreatedException;
import de.hsrm.mi.swtp.exchangeplatform.model.authentication.WhoAmI;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import de.hsrm.mi.swtp.exchangeplatform.model.data.PO;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.TypeOfUsers;
import de.hsrm.mi.swtp.exchangeplatform.repository.ModuleRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.PORepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * User service class for manipulating user data
 */
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
	
	UserRepository repository;
	ModuleRepository moduleRepository;
	PORepository poRepository;
	
	public List<User> getAll() {
		return repository.findAll();
	}
	
	public Optional<User> getById(Long userId) {
		return repository.findById(userId);
	}
	
	public Optional<User> getByUsername(String username) {
		return repository.findByUsername(username);
	}
	
	// convert number to string to find user only containing said number
	public List<User> getAllByStudentNumber(String studentNumber) { return repository.findByStudentNumberContaining(studentNumber);}
	
	// convert number to string to find user only containing said number
	public List<User> getAllByStaffNumber(String staffNumber) { return repository.findByStaffNumberContaining(staffNumber);}
	
	public List<User> getAllByFirstName(String firstName) { return repository.findAllByFirstNameContainingIgnoreCase(firstName); }
	
	public List<User> getAllByLastName(String lastName) { return repository.findAllByLastNameContainingIgnoreCase(lastName);}
	
	public List<User> getAllByPO(PO po) {
		return poRepository.findByTitleIs(po.getTitle()).getStudents();
	}
	
	public Long getUserTotalCPSelected(final User user) {
		Long totalCP = 0L;
		for(Timeslot timeslot : user.getTimeslots()) totalCP += timeslot.getModule().getCreditPoints();
		return totalCP;
	}
	
	public void save(User user) throws IllegalArgumentException {
		if(repository.existsById(user.getStudentNumber())) {
			log.info(String.format("FAIL: User %s not created. User already exists", user));
			throw new NotCreatedException(user);
		}
		repository.save(user);
		log.info(String.format("SUCCESS: User %s created", user));
	}
	
	public void delete(User user) {
		repository.delete(user);
	}
	
	public WhoAmI getWhoAmI(User user) {
		WhoAmI whoAmI = new WhoAmI();
		whoAmI.setUserId(user.getId());
		whoAmI.setUsername(user.getAuthenticationInformation().getUsername());
		whoAmI.setType(user.getUserType().getType());
		whoAmI.setRole(user.getAuthenticationInformation().getRole());
		return whoAmI;
	}
	
	public List<User> unifyLists(List<List<User>> lists) {
		List<User> out = new ArrayList<>();
		lists.forEach(list -> list.forEach(user -> {
			if(!out.contains(user)) out.add(user);
		}));
		return out;
	}
	
	public List<User> filterStaff(List<User> list) {
		for(User user : list){
			if(user.getUserType().getType() == TypeOfUsers.LECTURER){
				list.remove(user);
			}
		}
		return list;
	}
	
	public Boolean userPassedSemester(final User student, final Long semester) {
		final List<Module> completedModulesOfSemester = student.getCompletedModules()
															   .stream()
															   .filter(module -> module.getSemester().equals(semester))
															   .collect(Collectors.toList());
		final List<Module> modulesBySemester = moduleRepository.findModulesBySemesterIs(semester);
		return completedModulesOfSemester.size() >= modulesBySemester.size();
	}

}
