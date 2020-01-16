package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.model.data.PO;
import de.hsrm.mi.swtp.exchangeplatform.repository.PORepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class POService {

	PORepository repository;
	
	public List<PO> getAll() {
		return repository.findAll();
	}
	
	public Optional<PO> getById(Long poId) {
		return repository.findById(poId);
	}
	
	public List<PO> getAllDualStudy() {
		return repository.findAllDual();
	}
	
	public List<PO> getAllNonDualStudy() {
		return repository.findAllNonDual();
	}
	
}
