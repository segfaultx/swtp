package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.hsrm.mi.swtp.exchangeplatform.model.serializer.ModuleSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@ToString(exclude = { "po", "timeslots" })
@RequiredArgsConstructor
@Table(name = "my_module")
@JsonSerialize(using = ModuleSerializer.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Module implements Model {
	
	@Id
	@GeneratedValue
	Long id;
	
	String name;
	String contraction;
	Long moduleNumber;
	
	@Schema(defaultValue = "5", name = "credit_points", nullable = false)
	@JsonProperty(value = "credit_points", defaultValue = "5")
	Long creditPoints = 5L;
	
	@Schema(defaultValue = "1", name = "semester", nullable = false)
	@JsonProperty(value = "semester", defaultValue = "1")
	Long semester = 1L;
	
	@JsonIdentityReference
	@OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	List<Timeslot> timeslots;
	
	@ManyToMany(mappedBy = "modules", fetch = FetchType.EAGER)
	@JsonBackReference("attendee-module")
	List<User> attendees = new ArrayList<>();
	
	
	@JoinColumn(name = "po_id")
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonBackReference("po-modules")
	PO po;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	User lecturer;
	
	Boolean isActive;
}
