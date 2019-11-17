package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
public class Appointment {

    @Id
    @GeneratedValue
    private Long id;

    private String room;

    private String day;

    @JsonProperty("time_start")
    private LocalTime timeStart;

    @JsonProperty("time_end")
    private LocalTime timeEnd;

    private String lecturer;

    private String type;

    private int capacity;

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
