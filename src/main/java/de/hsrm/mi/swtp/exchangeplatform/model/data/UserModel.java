package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserModel implements Model {
	
	@Id
	@GeneratedValue
	Long id;
	
	String email;
	
	@JsonProperty("first_name")
	String firstName;
	
	@JsonProperty("last_name")
	String lastName;
	
	@JsonProperty("student_number")
	Long studentNumber;
	
	@JsonProperty("staff_number")
	Long staffNumber;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JsonIgnore
	AuthenticationInformation authenticationInformation;
	
	@JsonProperty("user_type")
	@OneToOne(cascade = CascadeType.ALL)
	@JsonManagedReference
	UserType userType;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	@JsonManagedReference
	List<Timeslot> timeslots;
}
