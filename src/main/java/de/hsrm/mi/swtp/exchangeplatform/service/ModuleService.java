package de.hsrm.mi.swtp.exchangeplatform.service;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.ModuleNotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import de.hsrm.mi.swtp.exchangeplatform.repository.ModuleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ModuleService {

    @Autowired(required = false)
    final JmsTemplate jmsTemplate;
    @Autowired
    final ModuleRepository repository;

    public List<Module> findAll() {
        return repository.findAll();
    }

    public Optional<Module> findById(Long moduleId) {
        return repository.findById(moduleId);
    }

    public Module getModuleById(Long moduleId) {
        Optional<Module> moduleOptional = this.findById(moduleId);
        if (!moduleOptional.isPresent()) throw new ModuleNotFoundException(moduleId);
        return moduleOptional.get();
    }

    public void save(Module module) {
        repository.save(module);
    }

    public void delete(Long moduleId) {
        repository.delete(this.getModuleById(moduleId));
    }
}
