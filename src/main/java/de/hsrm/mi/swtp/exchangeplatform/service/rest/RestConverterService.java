package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import org.springframework.stereotype.Service;

@Service
public interface RestConverterService {
	
	Object convert(Object object);
}
