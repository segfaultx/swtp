package de.hsrm.mi.swtp.exchangeplatform.messaging;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Model;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
public class ChangeEvent {
	
	@Setter(AccessLevel.NONE)
	@JsonProperty(value = "id")
	String type = "CHANGE";
	
	@JsonProperty(value = "model")
	Model model;
	
}
