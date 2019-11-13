package de.hsrm.mi.swtp.exchangeplatform.repository;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Timeslot, Long> {
    List<Timeslot> findAll();
    //Timeslot findByLecturer(Lecturer lecturer);
}
