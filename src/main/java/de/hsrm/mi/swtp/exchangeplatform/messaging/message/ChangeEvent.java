package de.hsrm.mi.swtp.exchangeplatform.messaging.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Model;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ChangeEvent {
	
	@Setter(AccessLevel.NONE)
	@JsonProperty(value = "id")
	String type = "CHANGE";
	
	@JsonProperty(value = "model")
	Model model;
	
}
