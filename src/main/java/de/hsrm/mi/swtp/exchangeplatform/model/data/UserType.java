package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.TypeOfUsers;
import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;


/**
 * @author Dennis S.
 */
@Entity
@Data
@ToString(exclude = {"user"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserType {
	
	@Id
	@GeneratedValue
	Long id;
	
	@Enumerated(EnumType.STRING)
	TypeOfUsers type;
	
	@OneToOne(mappedBy = "userType")
	@JsonBackReference("user-usertype")
	User user;
}
