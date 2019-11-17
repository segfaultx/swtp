package de.hsrm.mi.swtp.exchangeplatform.model.data;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
@Data
public class Student {

    @Id
    @Column(name = "matr_nr")
    private Long matriculationNumber;

    private String username;

    @ManyToMany(mappedBy = "attendees")
    private List<Timeslot> timeslots;

}
