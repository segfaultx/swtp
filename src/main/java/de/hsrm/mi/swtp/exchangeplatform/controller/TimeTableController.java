package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TimeTable;
import de.hsrm.mi.swtp.exchangeplatform.service.TimeTableService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/timetable")
public class TimeTableController {
    
    private final TimeTableService timeTableService;
    
    public TimeTableController(TimeTableService timeTableService) {
        this.timeTableService = timeTableService;
    }
    
    @GetMapping
    public ResponseEntity<List<TimeTable>> getAllTimeTables() {
        return new ResponseEntity<>(timeTableService.findAll(), HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TimeTable> getTimeTableById(@PathVariable Long id) {
        TimeTable found = timeTableService.findById(id)
                .orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(found, HttpStatus.OK);
    }
    
    @PostMapping
    public ResponseEntity<TimeTable> postTimeTable(@RequestBody TimeTable timeTable, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        
        timeTableService.save(timeTable);
        return new ResponseEntity<>(timeTable, HttpStatus.OK);
    }
}
