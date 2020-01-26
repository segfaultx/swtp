package de.hsrm.mi.swtp.exchangeplatform.model.rest;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;


//TODO: javadoc
@Data
@FieldDefaults(level =  AccessLevel.PRIVATE)
public class AdminSettingsRequest {
	boolean tradesActive;
	List<String> activeFilters;
	
}
