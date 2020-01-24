package de.hsrm.mi.swtp.exchangeplatform.repository;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import de.hsrm.mi.swtp.exchangeplatform.model.data.PO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
	List<Module> findAllByPoAndIsActive(PO po, boolean isActive);
	List<Module> findModulesBySemesterIsLessThanEqual(final Long semester);
	List<Module> findModulesBySemesterIs(final Long semester);
	
}