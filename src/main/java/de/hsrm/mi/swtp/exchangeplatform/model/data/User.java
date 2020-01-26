package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Dennis S.
 */
@Entity
@Data
@ToString(exclude = {"authenticationInformation", "userType", "timeslots", "tradeoffers", "modules"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User implements Model {
	
	@Id
	@GeneratedValue
	Long id;
	
	String email;
	
	@JsonProperty("first_name")
	String firstName;
	
	@JsonProperty("last_name")
	String lastName;
	
	@JsonProperty("student_number")
	@Schema(nullable = true)
	Long studentNumber;
	
	@JsonProperty("staff_number")
	@Schema(nullable = true)
	Long staffNumber;
	
	@JsonProperty("cp")
	int cp;
	
	@JsonProperty("fairness")
	int fairness;
	
	@JsonProperty(value = "current_semester", required = true)
	@Schema(defaultValue = "0", required = true, nullable = false, type = "integer", format = "int64")
	Long currentSemester = 0L;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JsonIgnore
	@JsonManagedReference("user-authinformation")
	AuthenticationInformation authenticationInformation;
	
	@JsonProperty("user_type")
	@OneToOne(cascade = CascadeType.ALL)
	@JsonManagedReference("user-usertype")
	UserType userType;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JsonBackReference("po-students")
	PO po;
	
	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL)
	@JsonManagedReference("user-timeslots")
	List<Timeslot> timeslots = new ArrayList<>();
	
	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL)
	@JsonManagedReference("student-waitlist")
	List<Timeslot> waitLists = new ArrayList<>();
	
	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL)
	@JsonManagedReference("attendee-module")
	List<Module> modules = new ArrayList<>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "offerer", cascade = CascadeType.ALL)
	@JsonManagedReference("offerer-tradeoffers")
	List<TradeOffer> tradeoffers = new ArrayList<>();
	
	@JsonProperty("completed_modules")
	@OneToMany(cascade = CascadeType.ALL)
	List<Module> completedModules = new ArrayList<>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "seeker", cascade = CascadeType.ALL)
	@JsonManagedReference("seeker-tradeoffers")
	List<TradeOffer> tradeofferSeeks = new ArrayList<>();
}
