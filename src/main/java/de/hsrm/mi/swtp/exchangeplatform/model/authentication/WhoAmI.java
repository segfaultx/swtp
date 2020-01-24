package de.hsrm.mi.swtp.exchangeplatform.model.authentication;

import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.Roles;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.TypeOfUsers;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Data object with userId, username, {@link TypeOfUsers} and {@link Roles}
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WhoAmI {
	
	Long userId;
	String username;
	TypeOfUsers type;
	Roles role;
}
