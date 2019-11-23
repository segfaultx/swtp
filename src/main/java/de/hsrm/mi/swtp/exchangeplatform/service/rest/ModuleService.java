package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
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
public class ModuleService implements RestService<Module, Long> {

    @Autowired(required = false)
    final JmsTemplate jmsTemplate;
    @Autowired
    final ModuleRepository repository;

    @Override
    public List<Module> getAll() {
        return repository.findAll();
    }

    @Override
    public Module getById(Long moduleId) {
        Optional<Module> moduleOptional = this.repository.findById(moduleId);
        if (!moduleOptional.isPresent()) throw new NotFoundException(moduleId);
        return moduleOptional.get();
    }

    @Override
    public void save(Module module) {
        repository.save(module);
    }

    @Override
    public void delete(Long moduleId) throws IllegalArgumentException {
        repository.delete(this.getById(moduleId));
    }

    @Override
    public boolean update(Long aLong, Module update) throws IllegalArgumentException {
        return true;
    }
}
