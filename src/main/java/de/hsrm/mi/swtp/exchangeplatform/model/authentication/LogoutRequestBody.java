package de.hsrm.mi.swtp.exchangeplatform.model.authentication;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Data object that holds the Username of the user that requests a logout
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LogoutRequestBody {
	
	String username;
	
}
