package de.hsrm.mi.swtp.exchangeplatform.repository;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TimeSlotRepository extends JpaRepository<Timeslot, Long> {
    List<Timeslot> findAllByModule(Module module);
}
