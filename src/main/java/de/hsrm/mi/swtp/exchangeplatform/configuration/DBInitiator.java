package de.hsrm.mi.swtp.exchangeplatform.configuration;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import de.hsrm.mi.swtp.exchangeplatform.model.data.*;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.DayOfWeek;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.Roles;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.TypeOfTimeslots;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.TypeOfUsers;
import de.hsrm.mi.swtp.exchangeplatform.model.rest_models.TimeslotType;
import de.hsrm.mi.swtp.exchangeplatform.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class DBInitiator implements ApplicationRunner {
	
	TimeslotRepository timeslotRepository;
	
	UserRepository userRepository;
	
	ModuleRepository moduleRepository;
	
	RoomRepository roomRepository;
	
	TradeOfferRepository tradeofferRepository;
	
	
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
		
		AuthenticationInformation chandlerAuthInfo = new AuthenticationInformation();
		chandlerAuthInfo.setRole(Roles.MEMBER);
		chandlerAuthInfo.setUser(chandler);
		chandlerAuthInfo.setPassword("chandler123");
		chandlerAuthInfo.setUsername("cbing001");
		
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
		
		var d12_out = roomRepository.save(d12);
		
		
		// START AFS Timeslots
		
		Timeslot afsVorlesung = new Timeslot();
		afsVorlesung.setTimeSlotType(TypeOfTimeslots.VORLESUNG);
		afsVorlesung.setCapacity(100);
		afsVorlesung.setDay(DayOfWeek.WEDNESDAY);
		afsVorlesung.setTimeStart(LocalTime.of(8, 15));
		afsVorlesung.setTimeEnd(LocalTime.of(9, 45));
		
		afsVorlesung.setRoom(d12_out);
		afsVorlesung.setTimeTable(null);
		
		
		// START AFS UEBUNG 1
		Timeslot afsUebung = new Timeslot();
		afsUebung.setTimeSlotType(TypeOfTimeslots.UEBUNG);
		afsUebung.setCapacity(50);
		afsUebung.setDay(DayOfWeek.WEDNESDAY);
		afsUebung.setTimeStart(LocalTime.of(10, 0));
		afsUebung.setTimeEnd(LocalTime.of(11, 30));
		
		afsUebung.setRoom(d12_out);
		afsUebung.setTimeTable(null);
		
		// END AFS UEBUNG 1
		
		// START AFS UEBUNG 2
		
		Timeslot afsUebung2 = new Timeslot();
		afsUebung2.setTimeSlotType(TypeOfTimeslots.UEBUNG);
		afsUebung2.setCapacity(50);
		afsUebung2.setDay(DayOfWeek.WEDNESDAY);
		afsUebung2.setTimeStart(LocalTime.of(11, 45));
		afsUebung2.setTimeEnd(LocalTime.of(13, 15));
		
		afsUebung2.setRoom(d12_out);
		afsUebung2.setTimeTable(null);
		
		// END AFS UEBUNG 2
		
		var krechel_out = userRepository.save(krechel);
		afsUebung.setUser(krechel_out);
		afsUebung2.setUser(krechel_out);
		afsVorlesung.setUser(krechel_out);
		userRepository.save(weitz);
		// SAVE Lecturers first so attendees can reference them
		
		
		List<Timeslot> toSave = new ArrayList<>();
		toSave.add(afsVorlesung);
		toSave.add(afsUebung);
		toSave.add(afsUebung2);
		
		d12_out.setTimeslots(toSave);
		
		// START ADD TIMESLOTS DENNIS
		
		List<Timeslot> dennisTimeslots = new ArrayList<>();
		dennisTimeslots.add(afsUebung);
		dennisTimeslots.add(afsVorlesung);
		
		// END ADD TIMESLOTS DENNIS
		
		// START ADD TIMESLOTS CHANDLER
		
		List<Timeslot> chandlerTimeslots = new ArrayList<>();
		chandlerTimeslots.add(afsVorlesung);
		chandlerTimeslots.add(afsUebung2);
		
		// END ADD TIMESLOTS CHANDLER
		
		// START SET TIMESLOTS FOR USERS
		
		dennis.setTimeslots(dennisTimeslots);
		chandler.setTimeslots(chandlerTimeslots);
		
		// END SET TIMESLOTS FOR USERS
		
		// START USERS LIST
		
		List<User> usersToSave = new ArrayList<>();
		usersToSave.add(dennis);
		usersToSave.add(chandler);
		
		//END USERS LIST
		
		// START TRADEOFFER DENNIS
		TradeOffer offer1 = new TradeOffer();
		offer1.setOfferer(dennis);
		offer1.setSeek(afsUebung2);
		offer1.setOffer(afsUebung);
		dennis.getTradeoffers().add(offer1);
		// END TRADEOFFER DENNIS
		
		System.out.println(String.format("DENNIS WITH ID: %d", dennis.getId()));
		userRepository.saveAll(usersToSave); // saving both at the same time to prevent detached entity exception
		
		log.info("Done saving timeTable...");
		
	}
}
