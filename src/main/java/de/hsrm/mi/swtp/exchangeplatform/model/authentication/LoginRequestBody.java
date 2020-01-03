package de.hsrm.mi.swtp.exchangeplatform.model.authentication;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequestBody {
	
	String username;
	String password;
	
}
