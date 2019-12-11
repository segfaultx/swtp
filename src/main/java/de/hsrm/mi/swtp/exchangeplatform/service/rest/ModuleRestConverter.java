package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import de.hsrm.mi.swtp.exchangeplatform.model.rest_models.ModuleDTO;
import org.springframework.stereotype.Service;

@Service
public class ModuleRestConverter implements RestModelConverter<ModuleDTO> {
	@Override
	public boolean isResponsible(Object object) {
		return object instanceof de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
	}
	
	@Override
	public ModuleDTO convertToRest(Object object) {
		Module module = (Module) object;
		ModuleDTO out = new ModuleDTO();
		out.setId(module.getId());
		out.setName(module.getName());
		return out;
	}
}
