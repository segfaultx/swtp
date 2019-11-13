package de.hsrm.mi.swtp.exchangeplatform.model.data;

import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@Getter
public class PO {
    @Id
    @GeneratedValue
    private Long id;


    private LocalTime year;


    private String study;

    public PO(LocalTime year, String study){
        this.year = year;
        this.study = study;
    }
    @OneToMany(
            mappedBy = "po",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Module> modules;
}
