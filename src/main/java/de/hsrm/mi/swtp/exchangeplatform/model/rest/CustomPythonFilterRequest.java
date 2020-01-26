package de.hsrm.mi.swtp.exchangeplatform.model.rest;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomPythonFilterRequest {
	String filterName;
	String code;
}
