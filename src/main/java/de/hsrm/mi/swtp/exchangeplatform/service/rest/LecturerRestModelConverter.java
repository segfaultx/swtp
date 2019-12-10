package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.model.rest_models.Lecturer;
import org.springframework.stereotype.Service;

@Service
public class LecturerRestModelConverter implements RestModelConverter<Lecturer> {
	@Override
	public boolean isResponsible(Object object) {
		return false;
	}
	
	@Override
	public Lecturer convertToRest(Object object) {
		de.hsrm.mi.swtp.exchangeplatform.model.data.Lecturer lecturer = (de.hsrm.mi.swtp.exchangeplatform.model.data.Lecturer) object;
		Lecturer out = new Lecturer();
		out.setMail(lecturer.getEmail());
		out.setName(lecturer.getLastName());
		
		return out;
	}
}
