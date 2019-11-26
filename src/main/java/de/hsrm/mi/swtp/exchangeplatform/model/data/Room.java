package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
public class Room implements Model {

    @Id
    @GeneratedValue
    private Long id;

    @JsonProperty("room_number")
    private String roomNumber;

    private String location;

    @JsonIgnore
    @OneToMany(
            mappedBy = "room",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Timeslot> timeslots;

}