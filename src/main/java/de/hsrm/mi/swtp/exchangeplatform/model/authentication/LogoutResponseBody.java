package de.hsrm.mi.swtp.exchangeplatform.model.authentication;

import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.Status;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Data object that holds a logout message and {@link Status}
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Data
public class LogoutResponseBody {
	
	String message;
	Status status;
	
}
