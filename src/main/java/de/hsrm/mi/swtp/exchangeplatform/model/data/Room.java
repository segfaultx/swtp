package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Room {

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

    public Room(String roomNumber, String location){
        this.roomNumber = roomNumber;
        this.location = location;
    }

}
