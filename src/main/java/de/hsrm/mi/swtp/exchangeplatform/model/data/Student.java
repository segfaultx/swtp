package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
@Data
public class Student implements Model {
	
	@Id
	@JsonProperty(value = "matriculationNumber", required = true)
	@Column(name = "matr_nr", unique = true, updatable = false, insertable = false)
	private Long matriculationNumber;
	
	@JsonProperty("username")
	@Column(unique = true)
	private String username;
	
	@JsonProperty("first_name")
	private String firstName;
	
	@JsonProperty("last_name")
	private String lastName;
	
	@JsonManagedReference
	@ManyToMany(mappedBy = "attendees")
	private List<Timeslot> timeslots;
	
}
