package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notcreated.NotCreatedException;
import de.hsrm.mi.swtp.exchangeplatform.messaging.sender.UserMessageSender;
import de.hsrm.mi.swtp.exchangeplatform.model.authentication.WhoAmI;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

	UserRepository repository;
	
	UserMessageSender messageSender;

	public List<User> getAll() {
		return repository.findAll();
	}

	public Optional<User> getById(Long userId) {
		return repository.findById(userId);
	}

	public Optional<User> getByUsername(String username) {
		return repository.findByUsername(username);
	}

	public void save(User user) throws IllegalArgumentException {
		if(repository.existsById(user.getStudentNumber())) {
			log.info(String.format("FAIL: User %s not created. User already exists", user));
			throw new NotCreatedException(user);
		}
		repository.save(user);
		log.info(String.format("SUCCESS: User %s created", user));
		
		messageSender.send(user);
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
}
