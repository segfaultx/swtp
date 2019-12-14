package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.TypeOfUsers;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserType {
	
	@Id
	@GeneratedValue
	Long id;
	
	@Enumerated(EnumType.STRING)
	TypeOfUsers type;
	
	@OneToOne(mappedBy = "userType")
	@JsonBackReference
	UserModel user;
}
