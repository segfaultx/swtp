package de.hsrm.mi.swtp.exchangeplatform.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
@Getter
@Setter
public class Student {
    @Id
    private String username;
    @ManyToMany
    private List<Appointment> appointments;

    public Student(String username){
        this.username = username;
    }

}
