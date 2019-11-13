package de.hsrm.mi.swtp.exchangeplatform.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Appointment {
    @Id
    @GeneratedValue
    private long id;
    private int room;
    private int capacity;
    @ManyToOne
    private Lecturer lecturer;
    private enum Type { LECTURE, LESSON }
    private int timeStart;
    private int timeEnd;
    @ManyToMany
    private List<Student> attendees;
    @ManyToOne
    private Module module;

    public Appointment(long id, int room, int capacity, Lecturer lecturer, int timeStart, int timeEnd) {
        this.id = id;
        this.room = room;
        this.capacity = capacity;
        this.lecturer = lecturer;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        attendees = new ArrayList<>();
    }

}
