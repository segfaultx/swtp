package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import org.springframework.stereotype.Service;

@Service
public interface RestModelConverter {
	
	boolean isResponsible(Object object);
	Object convertToRest(Object object);
	
}
