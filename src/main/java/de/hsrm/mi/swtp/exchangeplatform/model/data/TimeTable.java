package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class TimeTable {

    @Id @GeneratedValue
    private Long id;

    @JsonProperty("date_start")
    private LocalDate dateStart;

    @JsonProperty("date_end")
    private LocalDate dateEnd;

    @OneToMany(
            mappedBy = "timeTable",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Module> modules = new ArrayList<>();

}
