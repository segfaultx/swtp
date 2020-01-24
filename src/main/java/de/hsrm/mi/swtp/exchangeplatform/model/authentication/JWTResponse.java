package de.hsrm.mi.swtp.exchangeplatform.model.authentication;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;

/**
 * Helper class for JWTResponse that holds the token
 */
@Value
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class JWTResponse {
	
	String token;
	
}
