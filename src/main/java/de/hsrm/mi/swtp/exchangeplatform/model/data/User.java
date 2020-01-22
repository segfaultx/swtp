package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
	
	@OneToOne(cascade = CascadeType.ALL)
	@JsonIgnore
	AuthenticationInformation authenticationInformation;
	
	
	@JsonProperty("user_type")
	@OneToOne(cascade = CascadeType.ALL)
	@JsonManagedReference
	UserType userType;
	
	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL)
	@JsonManagedReference
	List<Timeslot> timeslots = new ArrayList<>();
	
	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL)
	@JsonManagedReference
	List<Timeslot> waitLists = new ArrayList<>();
	
	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL)
	@JsonManagedReference
	List<Module> modules = new ArrayList<>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "offerer", cascade = CascadeType.ALL)
	List<TradeOffer> tradeoffers = new ArrayList<>();
	
	@JsonProperty("completed_modules")
	@OneToMany(cascade = CascadeType.ALL)
	List<Module> completedModules = new ArrayList<>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "seeker", cascade = CascadeType.ALL)
	List<TradeOffer> tradeofferSeeks = new ArrayList<>();
}
