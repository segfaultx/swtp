package de.hsrm.mi.swtp.exchangeplatform.configuration.initiator;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import de.hsrm.mi.swtp.exchangeplatform.model.data.*;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.DayOfWeek;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.Roles;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.TypeOfTimeslots;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.TypeOfUsers;
import de.hsrm.mi.swtp.exchangeplatform.model.settings.AdminSettings;
import de.hsrm.mi.swtp.exchangeplatform.repository.AdminSettingsRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.RoomRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.UserRepository;
import de.hsrm.mi.swtp.exchangeplatform.service.settings.AdminSettingsService;
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

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class DBInitiator implements ApplicationRunner {
	
	UserRepository userRepository;
	
	RoomRepository roomRepository;
	
	AdminSettingsService adminSettingsService;
	
	AdminSettingsRepository adminSettingsRepository;
	
	
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
		
		// START Willi
		
		User willi = new User();
		willi.setFirstName("Willi");
		willi.setLastName("Wusel");
		willi.setStaffNumber(null);
		willi.setStudentNumber(1006555L);
		willi.setEmail("willi.wusel@student.hs-rm.de");
		
		AuthenticationInformation williInformation = new AuthenticationInformation();
		williInformation.setUsername("wwuse001");
		williInformation.setPassword("wwuse001");
		williInformation.setRole(Roles.MEMBER);
		williInformation.setUser(willi);
		
		UserType williType = new UserType();
		williType.setType(TypeOfUsers.STUDENT);
		williType.setUser(willi);
		
		willi.setAuthenticationInformation(williInformation);
		willi.setUserType(williType);
		
		// END Willi
		
		// START Jöndhard
		
		User joendhard = new User();
		joendhard.setFirstName("Jöndhard");
		joendhard.setLastName("Joendhardson");
		joendhard.setStaffNumber(null);
		joendhard.setStudentNumber(1006333L);
		joendhard.setEmail("joendhard.joendhardson@student.hs-rm.de");
		
		AuthenticationInformation joendhardInformation = new AuthenticationInformation();
		joendhardInformation.setUsername("jjoen001");
		joendhardInformation.setPassword("jjoen001");
		joendhardInformation.setRole(Roles.MEMBER);
		joendhardInformation.setUser(joendhard);
		
		UserType joendhardType = new UserType();
		joendhardType.setType(TypeOfUsers.STUDENT);
		joendhardType.setUser(joendhard);
		
		joendhard.setAuthenticationInformation(joendhardInformation);
		joendhard.setUserType(joendhardType);
		
		// END Jöndhard
		
		
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
		weitz.setUserType(weitzType);
		
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
		
		chandler.setAuthenticationInformation(chandlerAuthInfo);
		
		UserType chandlerType = new UserType();
		chandlerType.setType(TypeOfUsers.STUDENT);
		chandlerType.setUser(chandler);
		
		chandler.setUserType(chandlerType);
		
		
		// END CHANDLER
		
		// START FRODO
		
		User frodo = new User();
		frodo.setFirstName("Frodo");
		frodo.setLastName("Beutlin");
		frodo.setStudentNumber(1035123L);
		frodo.setEmail("frodo.beutlin@student.hs-rm.de");
		frodo.setStaffNumber(null);
		
		AuthenticationInformation frodoAuthInfo = new AuthenticationInformation();
		frodoAuthInfo.setRole(Roles.MEMBER);
		frodoAuthInfo.setUser(frodo);
		frodoAuthInfo.setPassword("fbeut001");
		frodoAuthInfo.setUsername("fbeut001");
		
		frodo.setAuthenticationInformation(frodoAuthInfo);
		
		UserType frodoType = new UserType();
		frodoType.setType(TypeOfUsers.STUDENT);
		frodoType.setUser(frodo);
		
		frodo.setUserType(frodoType);
		
		// END FRODO
		
		// START GANDALF
		
		User gandalf = new User();
		gandalf.setFirstName("Gandalf");
		gandalf.setLastName("Der_Graue");
		gandalf.setStudentNumber(1035146L);
		gandalf.setEmail("gandalf.der_graue@student.hs-rm.de");
		gandalf.setStaffNumber(null);
		
		AuthenticationInformation gandalfAuthInfo = new AuthenticationInformation();
		gandalfAuthInfo.setRole(Roles.MEMBER);
		gandalfAuthInfo.setUser(gandalf);
		gandalfAuthInfo.setPassword("gderg001");
		gandalfAuthInfo.setUsername("gderg001");
		
		gandalf.setAuthenticationInformation(gandalfAuthInfo);
		
		UserType gandalfType = new UserType();
		gandalfType.setType(TypeOfUsers.STUDENT);
		gandalfType.setUser(gandalf);
		
		gandalf.setUserType(gandalfType);
		
		// END GANDALF
		
		// START SAMWEIS
		
		User samweis = new User();
		samweis.setFirstName("Samweis");
		samweis.setLastName("Gamdschie");
		samweis.setStudentNumber(1035233L);
		samweis.setEmail("Samweis.Gamdschie@student.hs-rm.de");
		samweis.setStaffNumber(null);
		
		AuthenticationInformation samweisAuthInfo = new AuthenticationInformation();
		samweisAuthInfo.setRole(Roles.MEMBER);
		samweisAuthInfo.setUser(gandalf);
		samweisAuthInfo.setPassword("sgamd001");
		samweisAuthInfo.setUsername("sgamd001");
		
		samweis.setAuthenticationInformation(samweisAuthInfo);
		
		UserType samweisType = new UserType();
		samweisType.setType(TypeOfUsers.STUDENT);
		samweisType.setUser(samweis);
		
		samweis.setUserType(samweisType);
		
		// END SAMWEIS
		
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
		
		
		// START Modul Programmieren 2
		
		Module prog2 = new Module();
		prog2.setName("Programmieren 2");
		prog2.setPo(po2017);
		
		// END Modul Programmieren 2
		
		
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
		afsVorlesung.setModule(afs);
		afsVorlesung.setDay(DayOfWeek.WEDNESDAY);
		afsVorlesung.setTimeStart(LocalTime.of(8, 15));
		afsVorlesung.setTimeEnd(LocalTime.of(9, 45));
		
		afsVorlesung.setRoom(d12_out);
		afsVorlesung.setTimeTable(null);
		
		
		// START AFS UEBUNG 1
		Timeslot afsUebung = new Timeslot();
		afsUebung.setTimeSlotType(TypeOfTimeslots.UEBUNG);
		afsUebung.setCapacity(50);
		afsUebung.setModule(afs);
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
		afsUebung2.setModule(afs);
		afsUebung2.setDay(DayOfWeek.WEDNESDAY);
		afsUebung2.setTimeStart(LocalTime.of(11, 45));
		afsUebung2.setTimeEnd(LocalTime.of(13, 15));
		
		afsUebung2.setRoom(d12_out);
		afsUebung2.setTimeTable(null);
		
		// END AFS UEBUNG 2
		
		// START AFS UEBUNG 3
		
		Timeslot afsUebung3 = new Timeslot();
		afsUebung3.setTimeSlotType(TypeOfTimeslots.UEBUNG);
		afsUebung3.setCapacity(50);
		afsUebung3.setModule(afs);
		afsUebung3.setDay(DayOfWeek.MONDAY);
		afsUebung3.setTimeStart(LocalTime.of(10, 0));
		afsUebung3.setTimeEnd(LocalTime.of(11, 30));
		
		afsUebung3.setRoom(d12_out);
		afsUebung3.setTimeTable(null);
		
		// END AFS UEBUNG 3
		
		// START AFS UEBUNG 4
		
		Timeslot afsUebung4 = new Timeslot();
		afsUebung4.setTimeSlotType(TypeOfTimeslots.UEBUNG);
		afsUebung4.setCapacity(50);
		afsUebung4.setModule(afs);
		afsUebung4.setDay(DayOfWeek.FRIDAY);
		afsUebung4.setTimeStart(LocalTime.of(11, 45));
		afsUebung4.setTimeEnd(LocalTime.of(13, 15));
		
		afsUebung4.setRoom(d12_out);
		afsUebung4.setTimeTable(null);
		
		// END AFS UEBUNG 4
		
		
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
		
		// START ADD TIMESLOTS WILLI
		List<Timeslot> williTimeslots = new ArrayList<>();
		williTimeslots.add(afsVorlesung);
		williTimeslots.add(afsUebung3);
		
		// END ADD TIMESLOTS WILLI
		
		// START ADD TIMESLOTS JÖNDHARD
		List<Timeslot> joendhardTimeslots = new ArrayList<>();
		joendhardTimeslots.add(afsVorlesung);
		joendhardTimeslots.add(afsUebung4);
		
		// END ADD TIMESLOTS JÖNDHARD
		
		// START SET TIMESLOTS FOR USERS
		
		dennis.setTimeslots(dennisTimeslots);
		chandler.setTimeslots(chandlerTimeslots);
		joendhard.setTimeslots(joendhardTimeslots);
		willi.setTimeslots(williTimeslots);
		
		// END SET TIMESLOTS FOR USERS
		
		// START USERS LIST
		
		List<User> usersToSave = new ArrayList<>();
		usersToSave.add(dennis);
		usersToSave.add(chandler);
		usersToSave.add(joendhard);
		usersToSave.add(willi);
		
		//END USERS LIST
		
		// START TRADEOFFER DENNIS
		
		TradeOffer offer1 = new TradeOffer();
		offer1.setOfferer(dennis);
		offer1.setSeek(afsUebung2);
		offer1.setOffer(afsUebung);
		dennis.getTradeoffers().add(offer1);
		
		// END TRADEOFFER DENNIS
		
		// START TRADEOFFER JOENDHARD
		
		TradeOffer offer2 = new TradeOffer();
		offer2.setOfferer(joendhard);
		offer2.setOffer(afsUebung4);
		offer2.setSeek(afsUebung3);
		joendhard.getTradeoffers().add(offer2);
		
		// END TRADEOFFER JOENDHARD
		
		List<Module> completedModulesDennis = new ArrayList<>();
		completedModulesDennis.add(prog3);
		dennis.setCompletedModules(completedModulesDennis);
		
		
		System.out.println(String.format("DENNIS WITH ID: %d", dennis.getId()));
		userRepository.saveAll(usersToSave); // saving both at the same time to prevent detached entity exception
		
		AdminSettings adminSettings = new AdminSettings();
		adminSettings.setId(1);
		List<String> filters = new ArrayList<>();
		filters.add("COLLISION");
		filters.add("CAPACITY");
		adminSettings.updateAdminSettings(true, filters);
		
		var persistedSettings = adminSettingsRepository.save(adminSettings);
		adminSettingsService.setAdminSettings(persistedSettings);
		
		log.info("Done saving timeTable...");
	}
	
}
