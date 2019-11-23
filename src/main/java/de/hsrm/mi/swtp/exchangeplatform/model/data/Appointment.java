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
import java.util.stream.Collectors;

@Entity
@Data
@RequiredArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue
    private Long id;

    private String room;

    private String day;

    @JsonProperty("time_start")
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime timeStart;  // string nur weil aktuell in lokalen DB ein falscher Eintrag war

    @JsonProperty("time_end")
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime timeEnd;  // string nur weil aktuell in lokalen DB ein falscher Eintrag war

    private String lecturer;

    private String type;

    private int capacity;

    @ManyToMany
    private List<Student> attendees;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "module_id")
    private Module module;

    public boolean addAttendee(Student student) {
        List<Student> attendees = this.attendees
                .stream()
                .filter(student1 -> student1.getMatriculationNumber().equals(student.getMatriculationNumber()))
                .collect(Collectors.toList());
        return !(attendees.size() > 0) && this.attendees.add(student);
    }

}
