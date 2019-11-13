package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Getter
@Setter
public class Module {

    @Id @GeneratedValue private Long id;


    private String name;

    public Module(String name){
        this.name = name;
    }
    @OneToMany(
            mappedBy = "module",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Timeslot> timeslots;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private PO po;
}
