package de.hsrm.mi.swtp.exchangeplatform.model.data;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Getter
@Setter
public class Room {
    @Id
    @GeneratedValue
    private Long id;


    private String roomNumber;

    private String location;

    public Room(String roomNumber, String location){
        this.roomNumber = roomNumber;
        this.location = location;
    }

    @OneToMany(
            mappedBy = "room",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Timeslot> timeslots;
}
