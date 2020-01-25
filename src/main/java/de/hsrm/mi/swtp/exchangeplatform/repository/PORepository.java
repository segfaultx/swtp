package de.hsrm.mi.swtp.exchangeplatform.repository;

import de.hsrm.mi.swtp.exchangeplatform.model.data.PO;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PORepository extends JpaRepository<PO, Long> {
	
	PO findByTitleIs(String title);
	
}
