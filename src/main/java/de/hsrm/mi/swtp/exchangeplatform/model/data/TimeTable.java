package de.hsrm.mi.swtp.exchangeplatform.model.data;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
public class TimeTable {
    
    @Id
    @GeneratedValue
    private Long id;
    
    @Column(name = "start")
    private LocalDate dateStart;
    
    @Column(name = "end")
    private LocalDate dateEnd;
    
    @OneToMany(
            mappedBy = "timeTable",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Timeslot> timeslots = new ArrayList<>();
    
}
