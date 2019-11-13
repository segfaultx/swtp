package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
public class PO {
    @Id
    @GeneratedValue
    private Long id;

    @JsonProperty("valid_since_year")
    private String validSinceYear;

    private String major;

    public PO(String major){
        this.major = major;
    }
    @OneToMany(
            mappedBy = "po",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Module> modules;
}
