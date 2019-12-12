package de.hsrm.mi.swtp.exchangeplatform.configuration;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import de.hsrm.mi.swtp.exchangeplatform.model.data.*;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.DayOfWeek;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.Roles;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.TypeOfTimeslots;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.TypeOfUsers;
import de.hsrm.mi.swtp.exchangeplatform.repository.TimeTableRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.TradeOfferRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class DBInitiator implements ApplicationRunner {
	
	TimeTableRepository timeTableRepository;
	
	TradeOfferRepository tradeOfferRepository;
	
	UserRepository userRepository;
	
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		
		log.info("Filling Database with dark magic");
		
		User dennis = new User();
		dennis.setFirstName("Dennis");
		dennis.setLastName("Schad");
		dennis.setStaffNumber(null);
		dennis.setStudentNumber(1006351L);
		dennis.setEmail("dennis.schad@student.hs-rm.de");
		
		AuthenticationInformation dennisInformation = new AuthenticationInformation();
		dennisInformation.setUsername("dscha001");
		dennisInformation.setPassword("dscha001");
		dennisInformation.setRole(Roles.MEMBER);
		
		// TODO: check if necessary
		//dennisInformation.setUser(dennis);
		
		UserType dennisType = new UserType();
		dennisType.setType(TypeOfUsers.STUDENT);
		
		// TODO check if necessary
		//dennisType.setUser(dennis);
		
		dennis.setAuthenticationInformation(dennisInformation);
		dennis.setUserType(dennisType);
		
		dennis.setTimeslots(null);
		
		userRepository.save(dennis);
		
//		User lecturer = new User();
//		lecturer.setFirstName("Wolfgang");
//		lecturer.setLastName("Weitz");
//		lecturer.setEmail("wolfgang.weitz@hs-rm.de");
//		lecturer.getAuthenticationInformation().setUsername("wweit001");
//		lecturer.setStaffNumber(12344565678L);
//		lecturer.getUserType().setType(TypeOfUsers.LECTURER);
//
//		User dennis = new User();
//		dennis.setFirstName("Dennis");
//		dennis.setLastName("Schad");
//		dennis.setStudentNumber(1006351L);
//		dennis.getAuthenticationInformation().setUsername("dscha001");
//		dennis.getAuthenticationInformation().setRole("ADMIN");
//
//		Student willi = new Student();
//		willi.setFirstName("Willi");
//		willi.setLastName("Wusel");
//		willi.setStudentId(1005993L);
//		willi.setUsername("wwuse001");
//
//		Timeslot timeslot2 = new Timeslot();
//		timeslot2.setCapacity(20);
//		timeslot2.setDay(DayOfWeek.MONDAY);
//		timeslot2.setTimeStart(LocalTime.of(8, 15));
//		timeslot2.setTimeEnd(LocalTime.of(9, 45));
//		timeslot2.setTimeSlotType(TypeOfTimeslots.PRAKTIKUM);
//
//		Timeslot timeslot1 = new Timeslot();
//		timeslot1.setCapacity(20);
//		timeslot1.setDay(DayOfWeek.WEDNESDAY);
//		timeslot1.setTimeStart(LocalTime.of(8, 15));
//		timeslot1.setTimeEnd(LocalTime.of(9, 45));
//
//
//		Room d12 = new Room();
//		d12.setLocation("Unter den Eichen");
//		d12.setRoomNumber("D12");
//		timeslot1.setTimeSlotType(TypeOfTimeslots.PRAKTIKUM);
//
//
//		List<User> attendees = new ArrayList<>();
//		attendees.add(dennis);
//
//		List<Student> attendees2 = new ArrayList<>();
//		attendees2.add(willi);
//
//		List<Timeslot> timeslots = new ArrayList<>();
//		timeslots.add(timeslot1);
//		timeslots.add(timeslot2);
//
//		List<Timeslot> dennisTimeslots = new ArrayList<>();
//		dennisTimeslots.add(timeslot1);
//
//		List<Timeslot> williTimeslots = new ArrayList<>();
//		williTimeslots.add(timeslot2);
//
//		Module module = new Module();
//		module.setName("Softwaretechnik");
//		List<Module> modules = new ArrayList<>();
//		modules.add(module);
//
//		lecturer.setTimeslots(timeslots);
//		d12.setTimeslots(timeslots);
//
//		timeslot1.setRoom(d12);
//		timeslot2.setRoom(d12);
//
//		PO po = new PO();
//		po.setMajor("Medieninformatik");
//		po.setValidSinceYear("2017");
//		po.setModules(modules);
//
//		module.setPo(po);
//		module.setTimeslots(timeslots);
//
//		TimeTable timeTable = new TimeTable();
//		timeTable.setDateStart(LocalDate.of(2019, 10, 15));
//		timeTable.setDateEnd(LocalDate.of(2020, 3, 31));
//		timeTable.setTimeslots(timeslots);
//
//		timeslot1.setTimeTable(timeTable);
//		timeslot1.setModule(module);
//
//		TODO: Dennis anpassen nach model umstellung
//		timeslot1.setAttendees(attendees);
//		timeslot1.setLecturer(lecturer);
//
//		timeslot2.setTimeTable(timeTable);
//		timeslot2.setModule(module);
//
//		// TODO: Dennis anpassen nach Model Umstellung
//		timeslot2.setAttendees(attendees2);
//		timeslot2.setLecturer(lecturer);
//
//
//		dennis.setTimeslots(dennisTimeslots);
//		//willi.setTimeslots(williTimeslots);
//
//		TradeOffer tradeOffer = new TradeOffer();
//		//tradeOffer.setOfferer(willi);
//		tradeOffer.setOffer(timeslot2);
//		tradeOffer.setSeek(timeslot1);
//
//		tradeOfferRepository.save(tradeOffer);
//		timeTableRepository.save(timeTable);
		log.info("Done saving timeTable...");
		
	}
}
