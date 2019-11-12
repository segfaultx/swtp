package de.hsrm.mi.swtp.exchangeplatform.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Entity
public class Module {
    @Id
    private String name;
    private List<Appointment> appointments;

    public Module(String name){
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    public List<Appointment> getAppointments() {
        return appointments;
    }
}
