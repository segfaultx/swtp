package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Lecturer;
import de.hsrm.mi.swtp.exchangeplatform.model.rest_models.LecturerDTO;
import org.springframework.stereotype.Service;

@Service
public class LecturerRestModelConverter implements RestModelConverter<LecturerDTO> {
	@Override
	public boolean isResponsible(Object object) {
		return false;
	}
	
	@Override
	public LecturerDTO convertToRest(Object object) {
		Lecturer lecturer = (Lecturer) object;
		LecturerDTO out = new LecturerDTO();
		out.setMail(lecturer.getEmail());
		out.setName(lecturer.getLastName());
		return out;
	}
}
