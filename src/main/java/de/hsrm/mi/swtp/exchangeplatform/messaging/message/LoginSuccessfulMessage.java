package de.hsrm.mi.swtp.exchangeplatform.messaging.message;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class LoginSuccessfulMessage implements Serializable {

	String message;
	
}
