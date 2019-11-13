package de.hsrm.mi.swtp.exchangeplatform.model;

import com.github.javafaker.Faker;
import lombok.Builder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Builder
@Component
public class StudentFactory {

    private final static Faker faker = new Faker();

    public StudentDTO create() {
        return this.create(
                Math.abs(new Random().nextLong()),
                faker.name().fullName(),
                "Medieninformatik"
        );
    }

    /**
     * Creates a {@link StudentDTO} instance.
     *
     * @param matriculationNumber matriculation number of a student.
     * @param fullName            is a mocked full name of the returned student.
     * @param course              is the name of the study course.
     * @return a {@link StudentDTO} instance.
     */
    public StudentDTO create(final Long matriculationNumber, final String fullName, final String course) {
        return StudentDTO.builder()
                .matriculationNumber(matriculationNumber)
                .fullName(fullName)
                .course(course)
                .build();
    }

    public List<StudentDTO> create(final int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> this.create())
                .collect(Collectors.toList());
    }
}
