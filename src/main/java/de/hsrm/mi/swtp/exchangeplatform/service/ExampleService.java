package de.hsrm.mi.swtp.exchangeplatform.service;

import de.hsrm.mi.swtp.exchangeplatform.model.ExampleDataClass;
import de.hsrm.mi.swtp.exchangeplatform.repository.ExampleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ExampleService gives an example for a basic service class
 * TODO: Change this for a real service class
 */
public class ExampleService {

    private final ExampleRepository repository;

    @Autowired
    public ExampleService(ExampleRepository repository) {
        this.repository = repository;
    }

    public List<ExampleDataClass> findAll() {
        return null;
    }
}
