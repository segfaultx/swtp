package de.hsrm.mi.swtp.exchangeplatform.model.authentication;

import de.hsrm.mi.swtp.exchangeplatform.messaging.PersonalConnection;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * A simple object which will be returned when {@link User} has been
 * logged in successfully.
 * <p>
 * Contains necessary values such as the {@link JWTResponse} with a generated token (without which one cannot access
 * the api) and a personalized queue name. The latter is needed for the 1-1 connection between the server and a client.
 *
 * @see PersonalConnection
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Data
public class LoginResponseBody {
	
	JWTResponse tokenResponse;
	String queueName;
	
}
