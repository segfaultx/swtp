package de.hsrm.mi.swtp.exchangeplatform.repository;

import de.hsrm.mi.swtp.exchangeplatform.model.data.PO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PORepository extends JpaRepository<PO, Long> {
	
	@Query(value = "SELECT * from PO po where po.isDual = 'true'", nativeQuery = true)
	List<PO> findAllNonDual();
	
	@Query(value = "SELECT * from PO po where NOT po.isDual = 'true'", nativeQuery = true)
	List<PO> findAllDual();
	
}
