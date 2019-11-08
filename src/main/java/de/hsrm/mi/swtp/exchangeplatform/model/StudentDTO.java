package de.hsrm.mi.swtp.exchangeplatform.model;

import lombok.Builder;
import lombok.Value;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * This data class represents student data and stores it. It does only act as a transfer
 * object hence the lack of any setters.
 * This DTO is an @Entity-object for the good reason that all students will be provided
 * by a database and to define certain key property such as the {@code primary-key}
 * and mark those inside the DTO.
 *
 * @Data is a Lombok feature to generate boilerplate code. See Documentation for Details.
 * You might need to create the Lombok Plugin and enable Annotation Processing in your IDE
 */
@Entity // will be necessary when database is implemented
@Value
@Builder
public class StudentDTO {

    @Id
    Long matriculationNumber;
    String fullName;
    String course;

}
