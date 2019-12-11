package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
	@JsonProperty(value = "student_id", required = true)
	@Column(name = "student_id", unique = true, updatable = false, insertable = false)
	private Long studentId;
	
	@JsonProperty("username")
	@Column(unique = true)
	private String username;
	
	@JsonProperty("first_name")
	private String firstName;
	
	@JsonProperty("last_name")
	private String lastName;

	@JsonProperty("cp")
	private int cp;

	@JsonProperty("fairness")
	private int fairness;
	
	@ManyToMany(mappedBy = "attendees")
	private List<Timeslot> timeslots;
	
}
