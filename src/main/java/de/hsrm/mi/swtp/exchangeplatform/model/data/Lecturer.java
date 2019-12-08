package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
public class Lecturer {
	
	@Id
	private String username;
	
	private String email;
	
	@JsonProperty("first_name")
	private String firstName;
	
	@JsonProperty("last_name")
	private String lastName;
	
	@JsonIgnore
	@OneToMany(
			mappedBy = "lecturer",
			cascade = CascadeType.ALL,
			orphanRemoval = true
	)
	private List<Timeslot> timeslots = new ArrayList<>();
	
}
