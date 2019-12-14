package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notcreated.NotCreatedException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.UserModel;
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
	//UserMessageSender messageSender;

	public List<UserModel> getAll() {
		return repository.findAll();
	}

	public Optional<UserModel> getById(Long userId) {
		return repository.findById(userId);
	}

	public Optional<UserModel> getByUsername(String username) {
		return repository.findByUsername(username);
	}

	public void save(UserModel user) throws IllegalArgumentException {
		if(repository.existsById(user.getStudentNumber())) {
			log.info(String.format("FAIL: User %s not created. User already exists", user));
			throw new NotCreatedException(user);
		}
		repository.save(user);
		log.info(String.format("SUCCESS: User %s created", user));
		
		// TODO: nach bearbeitung von UserMessageSender wieder auskommentieren
		//messageSender.send(user);
	}

	public void delete(UserModel user) {
		repository.delete(user);
	}
}
