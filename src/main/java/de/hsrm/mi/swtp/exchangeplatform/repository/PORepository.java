package de.hsrm.mi.swtp.exchangeplatform.repository;

import de.hsrm.mi.swtp.exchangeplatform.model.data.PO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PORepository extends JpaRepository<PO, Long> {
	
	List<PO> findAllByValidSinceEquals(LocalDate date);
	
	PO findByTitleIs(String title);
	
}
