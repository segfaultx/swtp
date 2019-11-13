package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;

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

    @ManyToMany
    private List<Student> attendees;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id")
    private Module module;

}
