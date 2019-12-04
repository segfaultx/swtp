package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
@Table(name = "my_module")
public class Module implements Model {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @JsonBackReference
    @OneToMany(
            mappedBy = "module",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Timeslot> timeslots;

    @JsonIgnore
    @JoinColumn(name = "po_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private PO po;

}
