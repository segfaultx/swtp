package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
public class Timeslot {

    @Id
    @GeneratedValue
    @Column(name ="id")
    private Long id;

    @Column(name ="weekday")
    private Integer day;

    private enum Type { VORLESUNG, PRAKTIKUM, UEBUNG }

    @Column(name ="type")
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name ="time_start")
    private LocalTime timeStart;

    @Column(name="time_end")
    private LocalTime timeEnd;

    @ManyToOne
    private Lecturer lecturer;

    @Column(name = "max_capacity")
    private int capacity;

    @ManyToOne
    @JoinColumn(name ="room")
    private Room room;

    @ManyToOne
    @JoinColumn(name ="module")
    private Module module;

    @ManyToOne
    @JoinColumn(name ="courseplan")
    private TimeTable timeTable;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Student> attendees;

}
