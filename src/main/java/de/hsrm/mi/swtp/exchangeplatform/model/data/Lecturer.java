package de.hsrm.mi.swtp.exchangeplatform.model.data;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Lecturer {

    @Id
    private String usrname;

    private String email;

    private String firstName;

    @OneToMany(
            mappedBy = "lecturer",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Timeslot> timeslots = new ArrayList<>();



    public Lecturer(){
    }
}
