package de.hsrm.mi.swtp.exchangeplatform.service.admin;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.repository.StudentRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.TimeslotRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class DefaultAdminTradeService implements AdminTradeService {
	
	StudentRepository studentRepository;
	TimeslotRepository timeslotRepository;
	
	/**
	 * Method to asssign a new timeslot to a given student
	 *
	 * @param studentId      student id to lookup
	 * @param ownedTimeslot  id of owned timeslot
	 * @param futureTimeslot id of new timeslot
	 * @param adminId        id of admin
	 *
	 * @return returns new timeslot if successful
	 *
	 * @throws NotFoundException if any id couldnt be looked up
	 */
	@Override
	@Transactional
	public Timeslot assignTimeslotToStudent(long studentId, long ownedTimeslot, long futureTimeslot, long adminId) throws NotFoundException {
		log.info(String.format("Admin (ID: %d) assigning new timeslot (ID: %d) to student (ID: %d), old timeslot (ID: %d)", adminId, futureTimeslot, studentId,
							   ownedTimeslot
							  ));
		var student = studentRepository.findById(studentId).orElseThrow(() -> {
			log.info(String.format("Error looking up studentId: %d", studentId));
			throw new NotFoundException();
		});
		student.setTimeslots(student.getTimeslots().stream().filter(timeslot -> timeslot.getId() != ownedTimeslot).collect(Collectors.toList()));
		var timeslot = timeslotRepository.findById(futureTimeslot).orElseThrow(() -> {
			log.info(String.format("Error looking up future timeslot: ID %d", futureTimeslot));
			throw new NotFoundException();
		});
		student.getTimeslots().add(timeslot);
		studentRepository.save(student);
		return timeslot;
	}
}
