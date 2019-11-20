package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@RequiredArgsConstructor
public class Appointment {

    @JsonIgnore
    @Id
    @GeneratedValue
    private Long id;

    @JsonIgnore
    private String room;

    @JsonIgnore
    private String day;

    @JsonIgnore
    @JsonProperty("time_start")
    @JsonSerialize(using = LocalTimeSerializer.class)
    private String timeStart;  // string nur weil aktuell in lokalen DB ein falscher Eintrag war

    @JsonIgnore
    @JsonProperty("time_end")
    @JsonSerialize(using = LocalTimeSerializer.class)
    private String timeEnd;  // string nur weil aktuell in lokalen DB ein falscher Eintrag war

    @JsonIgnore
    private String lecturer;

    @JsonIgnore
    private String type;

    @JsonIgnore
    private int capacity;

    @JsonIgnore
    @ManyToMany
    private List<Student> attendees;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
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
