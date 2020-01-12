package de.hsrm.mi.swtp.exchangeplatform.model.authentication;

import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.Roles;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.TypeOfUsers;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WhoAmI {
	
	Long userId;
	String username;
	TypeOfUsers type;
	Roles role;
}