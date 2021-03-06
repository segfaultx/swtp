package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.ExchangeplatformStillActiveException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.PO;
import de.hsrm.mi.swtp.exchangeplatform.repository.PORepository;
import de.hsrm.mi.swtp.exchangeplatform.service.settings.AdminSettingsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service("poService")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class POService {

	PORepository repository;
	AdminSettingsService adminSettingsService;

	public PO getById(Long poId) throws NotFoundException {
		return repository.findById(poId).orElseThrow(NotFoundException::new);
	}

	public List<PO> getAll() {
		return repository.findAll();
	}

	//TODO: check if needed, not used
	public boolean update(PO update) throws IllegalArgumentException, NotFoundException {
		if(adminSettingsService.isTradesActive()) {
			throw new ExchangeplatformStillActiveException();
		}

		if(!repository.existsById(update.getId())) throw new NotFoundException();
		repository.save(update);

		return true;
	}

}
