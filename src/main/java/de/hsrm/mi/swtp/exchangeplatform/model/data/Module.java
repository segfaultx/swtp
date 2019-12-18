package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@ToString(exclude = {"po", "timeslots"})
@RequiredArgsConstructor
@Table(name = "my_module")
public class Module implements Model {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String name;
	
	@OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<Timeslot> timeslots;
	
	@JoinColumn(name = "po_id")
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private PO po;
	
}
