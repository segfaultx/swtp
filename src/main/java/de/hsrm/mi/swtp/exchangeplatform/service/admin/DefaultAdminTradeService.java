package de.hsrm.mi.swtp.exchangeplatform.service.admin;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.messaging.connectionmanager.TimeslotTopicManager;
import de.hsrm.mi.swtp.exchangeplatform.messaging.message.admin.po.ForceTradeSuccessfulMessage;
import de.hsrm.mi.swtp.exchangeplatform.messaging.sender.PersonalMessageSender;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.repository.TimeslotRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.UserRepository;
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
	
	UserRepository studentRepository;
	TimeslotRepository timeslotRepository;
	PersonalMessageSender personalMessageSender;
	TimeslotTopicManager timeslotTopicManager;
	
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
	public Timeslot assignTimeslotToStudent(long studentId, long ownedTimeslot, long futureTimeslot, long adminId) throws Exception {
		log.info(String.format("Admin (ID: %d) assigning new timeslot (ID: %d) to student (ID: %d), old timeslot (ID: %d)", adminId, futureTimeslot, studentId,
							   ownedTimeslot
							  ));
		var student = studentRepository.findById(studentId).orElseThrow(() -> {
			log.info(String.format("Error looking up studentId: %d", studentId));
			return new NotFoundException();
		});
		student.setTimeslots(student.getTimeslots().stream().filter(timeslot -> timeslot.getId() != ownedTimeslot).collect(Collectors.toList()));
		var timeslot = timeslotRepository.findById(futureTimeslot).orElseThrow(() -> {
			log.info(String.format("Error looking up future timeslot: ID %d", futureTimeslot));
			return new NotFoundException();
		});
		student.getTimeslots().add(timeslot);
		studentRepository.save(student);
		
		// notify user who is assigned to timeslots
		personalMessageSender.send(studentRepository.getOne(studentId),
								   ForceTradeSuccessfulMessage.builder()
															  .newTimeslot(timeslot)
															  .oldTimeslotId(ownedTimeslot)
															  .topic(timeslotTopicManager.getTopic(timeslot.getId()))
															  .build());
		
		log.info("FORCE TRADING...SUCCESS");
		
		return timeslot;
	}
}
