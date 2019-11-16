package de.hsrm.mi.swtp.exchangeplatform.service;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Appointment;
import de.hsrm.mi.swtp.exchangeplatform.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository repository;

    public AppointmentService(AppointmentRepository repository) {
        this.repository = repository;
    }

    public List<Appointment> findAll() {
        return repository.findAll();
    }

    public Optional<Appointment> findById(Long id) {
        return repository.findById(id);
    }

    public void save(Appointment appointment) {
        repository.save(appointment);
    }

    public boolean checkCapacity(Appointment appointment){
        return (appointment.getAttendees().size() < appointment.getCapacity());
    }
}
