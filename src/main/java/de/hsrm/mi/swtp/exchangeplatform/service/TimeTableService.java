package de.hsrm.mi.swtp.exchangeplatform.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TimeTable;
import de.hsrm.mi.swtp.exchangeplatform.repository.TimeTableRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TimeTableService {
    
    @Autowired
    TimeTableRepository repository;
    
    public List<TimeTable> findAll() {
        return repository.findAll();
    }
    
    public Optional<TimeTable> findById(Long id) {
        
        // This is just a Mock implementation
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        File jsonInput = new File(classLoader.getResource("data.json").getFile());
        
        try {
            return Optional.of(mapper.readValue(jsonInput, new TypeReference<TimeTable>() {
            }));
        } catch (IOException e) {
            return Optional.empty();
        }
        
    }
    
    public void save(TimeTable timeTable) {
        repository.save(timeTable);
    }
}
