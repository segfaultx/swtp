package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notcreated.NotCreatedException;
import de.hsrm.mi.swtp.exchangeplatform.messaging.sender.ModuleMessageSender;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import de.hsrm.mi.swtp.exchangeplatform.repository.ModuleRepository;
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
public class ModuleService {
	
	ModuleRepository repository;
	ModuleMessageSender messageSender;
	
	public List<Module> getAll() {
		return repository.findAll();
	}
	
	public Optional<Module> getById(Long moduleId) {
		Optional<Module> moduleOptional = this.repository.findById(moduleId);
		return moduleOptional;
	}
	
	public void save(Module module) {
		if(this.repository.existsById(module.getId())) {
			log.info(String.format("FAIL: Module %s not created", module));
			throw new NotCreatedException(module);
		}
		repository.save(module);
		messageSender.send(String.format("SUCCESS: Module %s created", module));
		log.info(String.format("SUCCESS: Module %s created", module));
	}
	
	public void delete(Module module) throws IllegalArgumentException {
		repository.delete(module);
		messageSender.send(String.format("SUCCESS: Module %s deleted", module));
		log.info(String.format("SUCCESS: Module %s deleted", module));
	}
}
