package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
@Data
public class Student {

    @Id @GeneratedValue
    private Long matriculationNumber;

    private String fullName;

    @JsonIgnore
    @ManyToMany
    private List<Appointment> appointments;

}
