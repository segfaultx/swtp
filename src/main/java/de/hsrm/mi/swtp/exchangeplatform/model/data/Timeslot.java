package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@Setter
@Getter
public class Timeslot {

    @Id
    @GeneratedValue
    @Column(name ="id")
    private Long id;

    @Column(name ="weekday")
    private Integer day;

    private enum Type { LECTURE, LESSON }

    @Column(name ="type")
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name ="start")
    private LocalTime timeStart;

    @Column(name="end")
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




    public Timeslot(Integer day, LocalTime timeStart, LocalTime timeEnd, int capacity){
        this.day = day;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.capacity = capacity;
    }

//Foreign-Keys:


    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Student> attendees;

}
