package de.hsrm.mi.swtp.exchangeplatform.service.admin.po.filter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.MessageType;
import de.hsrm.mi.swtp.exchangeplatform.model.admin.po.UserOccupancyViolationSerializer;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.RestrictionType;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@ToString(exclude = {"student"})
@RequiredArgsConstructor
@Slf4j
@Builder
@JsonSerialize(using = UserOccupancyViolationSerializer.class)
public class UserOccupancyViolation {
	
	@JsonProperty("type")
	MessageType messageType = MessageType.PO_VIOLATION;
	
	@JsonProperty("message")
	String message = "Die PO wurde aktualisiert und jetzt verstößt du gegen die PO. ";
	
	@JsonProperty("student")
	@JsonIgnore
	User student;
	
	@JsonProperty("violations")
	Map<RestrictionType, Object> violations;
}
