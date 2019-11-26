package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
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

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TimeTableService implements RestService<TimeTable, Long> {

    @Autowired
    TimeTableRepository repository;

    @Override
    public List<TimeTable> getAll() {
        return repository.findAll();
    }

    @Override
    public TimeTable getById(Long id) {
        // This is just a Mock implementation
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        File jsonInput = new File(classLoader.getResource("data.json").getFile());

        try {
            return mapper.readValue(jsonInput, new TypeReference<TimeTable>() {
            });
        } catch (IOException e) {
            throw new NotFoundException(id);
        }
    }

    public void save(TimeTable timeTable) {
        repository.save(timeTable);
    }

    @Override
    public void delete(Long id) throws IllegalArgumentException {
        this.repository.delete(this.getById(id));
    }

    @Override
    public boolean update(Long aLong, TimeTable update) throws IllegalArgumentException {
        return true;
    }

}
