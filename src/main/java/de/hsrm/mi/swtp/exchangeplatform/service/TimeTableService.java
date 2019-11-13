package de.hsrm.mi.swtp.exchangeplatform.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TimeTable;
import de.hsrm.mi.swtp.exchangeplatform.repository.TimeTableRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.List;
import java.util.Optional;

@Service
public class TimeTableService {

    private final TimeTableRepository repository;

    public TimeTableService(TimeTableRepository repository) {
        this.repository = repository;
    }

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
            return Optional.of(mapper.readValue(jsonInput, new TypeReference<TimeTable>() {}));
        } catch (IOException e) {
            return Optional.empty();
        }

    }

    public void save(TimeTable timeTable) {
        repository.save(timeTable);
    }
}
