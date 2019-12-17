package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.hsrm.mi.swtp.exchangeplatform.model.serializer.ModuleSerializer;
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
	@JsonIgnore
	private Long id;
	
	private String name;
	
	@OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<Timeslot> timeslots;
	
	@JoinColumn(name = "po_id")
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private PO po;
	
}
