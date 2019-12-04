package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.NoAppointmentCapacityException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.NotUpdatedException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.StudentIsAlreadyAttendeeException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notcreated.AppointmentNotCreatedException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.ModelNotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Student;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.repository.TimeslotRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TimeslotService implements RestService<Timeslot, Long> {
	
	@Autowired
	final JmsTemplate jmsTemplate;
	@Autowired
	final TimeslotRepository repository;
	
	@NonFinal
	private Integer attendeeCount = 0; // TODO: remove; is just for testing
	
	@Override
	public List<Timeslot> getAll() {
		return repository.findAll();
	}
	
	@Override
	public Timeslot getById(Long appointmentId) {
		Optional<Timeslot> appointmentOptional = this.repository.findById(appointmentId);
		if(!appointmentOptional.isPresent()) throw new NotFoundException(appointmentId);
		return appointmentOptional.get();
	}
	
	@Override
	public void save(Timeslot timeslot) {
		if(this.repository.existsById(timeslot.getId())) {
			log.info(String.format("FAIL: Appointment %s not created", timeslot));
			throw new AppointmentNotCreatedException(timeslot);
		}
		repository.save(timeslot);
		log.info(String.format("SUCCESS: Appointment %s created", timeslot));
	}
	
	@Override
	public void delete(Long id) throws IllegalArgumentException {
		this.repository.delete(this.getById(id));
		log.info(String.format("SUCCESS: Appointment %s deleted", id));
	}
	
	@Override
	public boolean update(Long appointmentId, Timeslot update) throws IllegalArgumentException {
		Timeslot timeslot = this.getById(appointmentId);
		
		if(!Objects.equals(timeslot.getId(), update.getId())) throw new NotUpdatedException();
		
		log.info("Updating appointment..");
		log.info(timeslot.toString() + " -> " + update.toString());
		timeslot.setCapacity(update.getCapacity());
		timeslot.setDay(update.getDay());
		timeslot.setLecturer(update.getLecturer());
		timeslot.setModule(update.getModule());
		timeslot.setRoom(update.getRoom());
		timeslot.setTimeStart(update.getTimeStart());
		timeslot.setTimeEnd(update.getTimeEnd());
		timeslot.setType(update.getType());
		
		this.save(timeslot);
		
		return true;
	}
	
	public void addAttendeeToAppointment(Long appointmentId, Student student) {
		Timeslot timeslot = this.getById(appointmentId);
		
		if(timeslot.getAttendees().contains(student)) {
			log.info(String.format("FAIL: Student %s is already an attendee", student.getMatriculationNumber()));
			throw new StudentIsAlreadyAttendeeException(student);
		}
		
		jmsTemplate.convertAndSend("AppointmentQueue", timeslot);
		
		if(!this.checkCapacity(timeslot) && !timeslot.addAttendee(student)) {
			log.info(String.format("FAIL: Student %s not added to appointment %s", student.getMatriculationNumber(), appointmentId));
			throw new NoAppointmentCapacityException(timeslot);
		}
		attendeeCount++;
		log.info(String.format("SUCCESS: Student %s added to appointment %s", student.getMatriculationNumber(), appointmentId));
	}
	
	public boolean checkCapacity(Timeslot timeslot) {
		return attendeeCount < timeslot.getCapacity();
	}
	
	public void removeAttendeeFromAppointment(Long appointmentId, Student student) {
		Timeslot timeslot = this.getById(appointmentId);
		
		jmsTemplate.convertAndSend("AppointmentQueue", timeslot);
		
		if(!timeslot.removeAttendee(student)) {
			log.info(String.format("FAIL: Student %s not removed", student.getMatriculationNumber()));
			throw new ModelNotFoundException(student);
		}
		attendeeCount--;
		log.info(String.format("SUCCESS: Student %s removed from appointment %s", student.getMatriculationNumber(), appointmentId));
	}
	
}
