package de.hsrm.mi.swtp.exchangeplatform.messaging.message.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.enums.MessageType;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.model.serializer.admin.AdminStudentStatusChangeMessageSerializer;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@JsonSerialize(using = AdminStudentStatusChangeMessageSerializer.class)
public class AdminStudentStatusChangeMessage implements Serializable {
	
	@JsonProperty("type")
	MessageType messageType;
	
	@JsonProperty("message")
	String message = "Studenten-Timeslots aktualisiert.";
	
	@JsonProperty("student")
	User student;
	
	@Builder
	public AdminStudentStatusChangeMessage(MessageType messageType, User student) {
		this.messageType = messageType;
		this.student = student;
	}
}
