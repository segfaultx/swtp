package de.hsrm.mi.swtp.exchangeplatform.configuration;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import de.hsrm.mi.swtp.exchangeplatform.model.data.*;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.DayOfWeek;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.Roles;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.TypeOfTimeslots;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.TypeOfUsers;
import de.hsrm.mi.swtp.exchangeplatform.repository.TimeTableRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.TimeslotRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.TradeOfferRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class DBInitiator implements ApplicationRunner {
	
	TimeslotRepository timeslotRepository;
	
	UserRepository userRepository;
	
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		
		log.info("Filling Database with dark magic");
		
		// START Dennis
		
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
		dennisInformation.setUser(dennis);
		
		UserType dennisType = new UserType();
		dennisType.setType(TypeOfUsers.STUDENT);
		dennisType.setUser(dennis);
		
		dennis.setAuthenticationInformation(dennisInformation);
		dennis.setUserType(dennisType);
		
		// END Dennis
		
		// START Weitz
		
		User weitz = new User();
		weitz.setFirstName("Wolfgang");
		weitz.setLastName("Weitz");
		weitz.setStudentNumber(null);
		weitz.setStaffNumber(171717171717L);
		weitz.setEmail("wolfgang.weitz@hs-rm.de");
		
		AuthenticationInformation weitzInformation = new AuthenticationInformation();
		weitzInformation.setUsername("wweit001");
		weitzInformation.setPassword("wweit001");
		weitzInformation.setRole(Roles.ADMIN);
		weitzInformation.setUser(weitz);
		weitz.setAuthenticationInformation(weitzInformation);
		
		UserType weitzType = new UserType();
		weitzType.setType(TypeOfUsers.LECTURER);
		weitzType.setUser(weitz);
		
		// END WEITZ
		
		// START CHANDLER
		
		User chandler = new User();
		chandler.setFirstName("Chandler");
		chandler.setLastName("Bing");
		chandler.setStudentNumber(1005917L);
		chandler.setEmail("chandler.bing@student.hs-rm.de");
		chandler.setStaffNumber(null);
		
		AuthenticationInformation williAuthInfo = new AuthenticationInformation();
		williAuthInfo.setRole(Roles.MEMBER);
		williAuthInfo.setUser(chandler);
		williAuthInfo.setPassword("chandler123");
		williAuthInfo.setUsername("cbing001");
		
		UserType chandlerType = new UserType();
		chandlerType.setType(TypeOfUsers.STUDENT);
		chandlerType.setUser(chandler);
		
		// END CHANDLER
		
		// START KRECHEL
		User krechel = new User();
		krechel.setFirstName("Dirk");
		krechel.setLastName("Krechel");
		krechel.setEmail("dirk.krechel@hs-rm.de");
		krechel.setStudentNumber(null);
		krechel.setStaffNumber(12345678L);
		krechel.setTimeslots(null);
		
		// KRECHEL USERTYPE
		UserType krechelType = new UserType();
		krechelType.setType(TypeOfUsers.LECTURER);
		krechelType.setUser(krechel);
		krechel.setUserType(krechelType);
		// END KRECHEL USERTYPE
		
		// KRECHEL AUTHINFO
		AuthenticationInformation krechelAuthInfo = new AuthenticationInformation();
		krechelAuthInfo.setUsername("dkrec001");
		krechelAuthInfo.setPassword("dkrec001");
		krechelAuthInfo.setUser(krechel);
		krechelAuthInfo.setRole(Roles.ADMIN);
		
		krechel.setAuthenticationInformation(krechelAuthInfo);
		//END KRECHEL AUTHINFO
		
		
		// END KRECHEL
		// START PO 2017
		
		PO po2017 = new PO();
		po2017.setMajor("Medieninformatik");
		po2017.setValidSinceYear("2017");
		
		// END PO 2017
		
		// START Modul AFS
		
		Module afs = new Module();
		afs.setName("Automaten und formale Sprachen");
		afs.setPo(po2017);
		
		// END Modul AFS
		
		// START Modul Programmieren 3
		
		Module prog3 = new Module();
		prog3.setName("Programmieren 3");
		prog3.setPo(po2017);
		
		// END Modul Programmieren 3
		
		// START ROOM D12
		Room d12 = new Room();
		d12.setLocation("Unter den Eichen");
		d12.setRoomNumber("D12");
		d12.setTimeslots(null);
		
		
		// START AFS Timeslots
		
		Timeslot afsVorlesung = new Timeslot();
		afsVorlesung.setTimeSlotType(TypeOfTimeslots.VORLESUNG);
		afsVorlesung.setCapacity(100);
		afsVorlesung.setDay(DayOfWeek.WEDNESDAY);
		afsVorlesung.setTimeStart(LocalTime.of(8, 15));
		afsVorlesung.setTimeEnd(LocalTime.of(9, 45));
		
		afsVorlesung.setUser(krechel);
		
		afsVorlesung.setModule(null);
		afsVorlesung.setRoom(d12);
		afsVorlesung.setTimeTable(null);
		
		
		Timeslot afsUebung = new Timeslot();
		afsUebung.setTimeSlotType(TypeOfTimeslots.UEBUNG);
		afsUebung.setCapacity(50);
		afsUebung.setDay(DayOfWeek.WEDNESDAY);
		afsUebung.setTimeStart(LocalTime.of(10, 0));
		afsUebung.setTimeEnd(LocalTime.of(11, 30));
		
		afsUebung.setUser(krechel);
		
		afsUebung.setModule(null);
		afsUebung.setRoom(d12);
		afsUebung.setTimeTable(null);
		
		List<Timeslot> dennisTimeslots = new ArrayList<>();
		dennisTimeslots.add(afsUebung);
		dennisTimeslots.add(afsVorlesung);
		
		dennis.setTimeslots(dennisTimeslots);
		
		
		userRepository.save(krechel);
		userRepository.save(weitz);
		// SAVE Lecturers first so attendees can reference them
		
		userRepository.save(dennis);
		userRepository.save(chandler);
		// TODO: Sinnvoll befüllen nach Model refactoring
		
		log.info("Done saving timeTable...");
		
	}
}
