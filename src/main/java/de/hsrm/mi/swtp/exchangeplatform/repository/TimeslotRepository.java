package de.hsrm.mi.swtp.exchangeplatform.repository;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeslotRepository extends JpaRepository<Timeslot, Long> {
	List<Timeslot> findAllByModule(Module module);
	
	@Query("select user.id from UserModel user join Timeslot ts where ts.id = :id")
	List<Timeslot> findAllAttendeesByTimeSlotId(@Param("id") Long id);
}
