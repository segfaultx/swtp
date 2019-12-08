package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ConverterServiceImpl implements RestConverterService {
	
	List<RestModelConverter> restModelConverters;
	
	@Override
	public Object convert(Object object) {
		
		for(RestModelConverter converter : restModelConverters) {
			if(converter.isResponsible(object)) return converter.convertToRest(object);
		}
		throw new RuntimeException("Unsupported type");
	}
}
