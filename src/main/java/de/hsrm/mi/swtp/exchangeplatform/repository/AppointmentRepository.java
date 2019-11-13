package de.hsrm.mi.swtp.exchangeplatform.repository;

import de.hsrm.mi.swtp.exchangeplatform.model.Appointment;
import de.hsrm.mi.swtp.exchangeplatform.model.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findAll();
    Appointment findByLecturer(Lecturer lecturer);
}
