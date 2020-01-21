package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.hsrm.mi.swtp.exchangeplatform.model.serializer.ModuleSerializer;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@ToString(exclude = {"po", "timeslots"})
@RequiredArgsConstructor
@Table(name = "my_module")
@JsonSerialize(using = ModuleSerializer.class)
public class Module implements Model {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String name;
	
	@JsonIdentityReference
	@OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<Timeslot> timeslots;
	
	@ManyToMany(mappedBy = "modules", fetch = FetchType.EAGER)
	@JsonBackReference
	List<User> attendees = new ArrayList<>();
	
	
	@JoinColumn(name = "po_id")
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private PO po;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;
}
