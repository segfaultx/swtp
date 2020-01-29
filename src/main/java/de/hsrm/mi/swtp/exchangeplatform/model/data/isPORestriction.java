package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface isPORestriction {
	@JsonIgnore
	boolean isActive();
}
