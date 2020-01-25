package de.hsrm.mi.swtp.exchangeplatform.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ModuleRequestBody {
	
	@JsonProperty("module_id")
	Long moduleId;
	
	@JsonProperty("student_id")
	Long studentId;
	
}