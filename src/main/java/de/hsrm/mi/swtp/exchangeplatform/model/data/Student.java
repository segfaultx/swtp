package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Student implements Model {
	
	@Id
	@JsonProperty(value = "student_id", required = true)
	@Column(name = "student_id", unique = true, updatable = false, insertable = false)
	Long studentId;
	
	@JsonProperty("username")
	@Column(unique = true)
	String username;
	
	@JsonProperty("first_name")
	String firstName;
	
	@JsonProperty("last_name")
	String lastName;
	
	String role = "MEMBER";
	
	@ManyToMany(mappedBy = "attendees")
	List<Timeslot> timeslots;
	
}
