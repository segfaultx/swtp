package de.hsrm.mi.swtp.exchangeplatform.repository;

import de.hsrm.mi.swtp.exchangeplatform.model.ExampleDataClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Example gives an example for a basic JPA Repository interface
 * TODO: Change this for a real interface
 */
@Repository
public interface ExampleRepository extends JpaRepository<ExampleDataClass, Long> {
}
