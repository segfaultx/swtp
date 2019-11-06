package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.model.ExampleDataClass;
import de.hsrm.mi.swtp.exchangeplatform.service.ExampleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ExampleRestController gives an example for a basic controller class
 * TODO: Change this for a real controller class
 */
@RestController
@RequestMapping("/api/v1/")
public class ExampleRestController {

    private final ExampleService service;

    public ExampleRestController(ExampleService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ExampleDataClass>> getExamples() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

}
