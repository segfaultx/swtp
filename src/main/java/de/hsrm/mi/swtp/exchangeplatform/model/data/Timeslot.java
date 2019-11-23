package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
public class Timeslot implements Model {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "weekday")
    private Integer day;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private Type type;

    @JsonIgnore
    @JsonProperty("time_start")
    @JsonSerialize(using = LocalTimeSerializer.class)
    @Column(name = "time_start")
    private LocalTime timeStart;

    @JsonIgnore
    @JsonProperty("time_end")
    @JsonSerialize(using = LocalTimeSerializer.class)
    @Column(name = "time_end")
    private LocalTime timeEnd;

    @JsonIgnore
    @ManyToOne
    private Lecturer lecturer;

    @JsonIgnore
    @Column(name = "max_capacity")
    private int capacity;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "room")
    private Room room;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "module")
    private Module module;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "courseplan")
    private TimeTable timeTable;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Student> attendees;

    private enum Type {VORLESUNG, PRAKTIKUM, UEBUNG}

}
