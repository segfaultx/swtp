package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.model.rest_models.Module;
import org.springframework.stereotype.Service;

@Service
public class ModuleRestConverter implements RestModelConverter<Module> {
	@Override
	public boolean isResponsible(Object object) {
		return object instanceof de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
	}
	
	@Override
	public Module convertToRest(Object object) {
		de.hsrm.mi.swtp.exchangeplatform.model.data.Module module = (de.hsrm.mi.swtp.exchangeplatform.model.data.Module) object;
		Module out = new Module();
		out.setId(module.getId());
		out.setName(module.getName());
		return out;
	}
}
