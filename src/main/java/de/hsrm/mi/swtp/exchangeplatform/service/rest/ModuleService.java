package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notcreated.NotCreatedException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
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
public class ModuleService implements RestService<Module, Long> {
	
	ModuleRepository repository;
	ModuleMessageSender messageSender;
	
	@Override
	public List<Module> getAll() {
		return repository.findAll();
	}
	
	@Override
	public Module getById(Long moduleId) {
		Optional<Module> moduleOptional = this.repository.findById(moduleId);
		if(!moduleOptional.isPresent()) {
			log.info(String.format("FAIL: Module %s not found", moduleId));
			throw new NotFoundException(moduleId);
		}
		return moduleOptional.get();
	}
	
	@Override
	public void save(Module module) {
		if(this.repository.existsById(module.getId())) {
			log.info(String.format("FAIL: Module %s not created", module));
			throw new NotCreatedException(module);
		}
		repository.save(module);
		messageSender.send(String.format("SUCCESS: Module %s created", module));
		log.info(String.format("SUCCESS: Module %s created", module));
	}
	
	@Override
	public void delete(Long moduleId) throws IllegalArgumentException {
		this.repository.delete(this.getById(moduleId));
		messageSender.send(String.format("SUCCESS: Module %s deleted", moduleId));
		log.info(String.format("SUCCESS: Module %s deleted", moduleId));
	}
	
	@Override
	public boolean update(Long aLong, Module update) throws IllegalArgumentException {
		return true;
	}
}
