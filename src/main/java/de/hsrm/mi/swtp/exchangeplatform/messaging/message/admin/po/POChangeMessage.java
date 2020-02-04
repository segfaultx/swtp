package de.hsrm.mi.swtp.exchangeplatform.messaging.message.admin.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.enums.MessageType;
import de.hsrm.mi.swtp.exchangeplatform.model.data.PO;
import de.hsrm.mi.swtp.exchangeplatform.model.serializer.admin.po.POChangeMessageSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Value
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@JsonSerialize(using = POChangeMessageSerializer.class)
@RequiredArgsConstructor
@Builder
public class POChangeMessage implements Serializable {
	
	@JsonProperty(value = "type", required = true)
	@Schema(nullable = false, required = true, format = "string", type = "string")
	private final MessageType messageType = MessageType.PO_CHANGED;
	
	@JsonProperty(value = "message", defaultValue = "", required = true)
	@Schema(nullable = false, required = true, format = "string", type = "string")
	String message = "Die PO %shat sich geändert.";
	
	PO po;
	
	public String getMessage() {
		return String.format(message, po == null ? "" : "\"" + po.getTitle() + "\" ");
	}
}
