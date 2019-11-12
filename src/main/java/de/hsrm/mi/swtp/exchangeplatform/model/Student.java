package de.hsrm.mi.swtp.exchangeplatform.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Entity
public class Student {
    @Id
    private String username;
    private List<Appointment> appointments;

    public Student(String username){
        this.username = username;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }
}
