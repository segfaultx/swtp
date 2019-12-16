package de.hsrm.mi.swtp.exchangeplatform.messaging;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.hsrm.mi.swtp.exchangeplatform.model.serializer.ExchangeplatformMessageSerializer;
import de.hsrm.mi.swtp.exchangeplatform.model.serializer.ModuleSerializer;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonSerialize(using = ExchangeplatformMessageSerializer.class)
public class ExchangeplatformStatusMessage implements Serializable {
	
	@Enumerated(EnumType.STRING)
	Status status = Status.INACTIVE;
	String message;
	
	@Builder
	public ExchangeplatformStatusMessage(String message) {
		this(false, message);
	}
	
	@Builder
	public ExchangeplatformStatusMessage(Boolean isActive, String message) {
		this.status = isActive ? Status.ACTIVE : Status.INACTIVE;
		this.message = message;
	}
	
	public enum Status {
		ACTIVE, INACTIVE
	}
	
}
