package de.hsrm.mi.swtp.exchangeplatform.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Getter
@Setter
public class Module {
    @Id
    private String name;
    @OneToMany(mappedBy = "module")
    private List<Appointment> appointments;

    public Module(String name){
        this.name = name;
    }

}
