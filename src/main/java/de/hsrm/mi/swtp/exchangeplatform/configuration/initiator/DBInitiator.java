package de.hsrm.mi.swtp.exchangeplatform.configuration.initiator;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import de.hsrm.mi.swtp.exchangeplatform.model.data.*;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.DayOfWeek;
import de.hsrm.mi.swtp.exchangeplatform.model.factory.*;
import de.hsrm.mi.swtp.exchangeplatform.model.admin.settings.AdminSettings;
import de.hsrm.mi.swtp.exchangeplatform.repository.AdminSettingsRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.PORepository;
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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class DBInitiator implements ApplicationRunner {
	
	ModuleFactory moduleFactory;
	POFactory poFactory;
	PORestrictionFactory poRestrictionFactory;
	RoomFactory roomFactory;
	TimeslotFactory timeslotFactory;
	UserFactory userFactory;
	AdminSettingsRepository adminSettingsRepository;
	UserRepository userRepository;
	RoomRepository roomRepository;
	PORepository poRepository;
	AdminSettingsService adminSettingsService;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		
		log.info("Filling Database with dark magic");
		
		// START Dennis
		User dennis = userFactory.createStudent("Dennis", "Schad", 1006351L);
		// END Dennis
		
		// START Willi
		User willi = userFactory.createStudent("Willi", "Wusel", 1006555L);
		// END Willi
		
		// START Jöndhard
		User joendhard = userFactory.createStudent("Jöndhard", "Joendhardson", 1006333L);
		// END Jöndhard
		
		// START Weitz
		User weitz = userFactory.createLecturerADMIN("Wolfgang", "Weitz", 171717171717L);
		// END WEITZ
		
		// START FRITZ
		User fritz = userFactory.createLecturer("Fritzchen", "Fritz", 16161616L);
		// END fritz
		
		// START Schwanecke
		User schwanecke = userFactory.createLecturerADMIN("Ulrich", "Schwanecke", 16161616L);
		// END schwanecke
		
		// START CHANDLER
		User chandler = userFactory.createStudent("Chandler", "Bing", 1005917L);
		// END CHANDLER
		
		// START FRODO
		User frodo = userFactory.createStudent("Frodo", "Beutlin", 1035123L);
		// END FRODO
		
		// START GANDALF
		User gandalf = userFactory.createStudent("Gandalf", "Der_Graue", 1035146L);
		// END GANDALF
		
		// START SAMWEIS
		User samweis = userFactory.createStudent("Samweis", "Gamdschie", 1035233L);
		// END SAMWEIS
		
		// START KRECHEL
		User krechel = userFactory.createLecturerADMIN("Dirk", "Krechel", 12345678L);
		// END KRECHEL
		
		// START PO 2017
		PORestriction restriction2017 = poRestrictionFactory.createPO();
		PO po2017 = poFactory.createPO(LocalDate.now().minusYears(3L), LocalDate.now().minusYears(3L), LocalDate.now().plusYears(3L));
		po2017.setRestriction(restriction2017);
		// END PO 2017
		
		// START Modul AFS
		Module afs = moduleFactory.createModule("Automaten und formale Sprachen", po2017, 3L);
		// END Modul AFS
		
		// START Modul CG
		Module cg = moduleFactory.createModule("Computergrafiken", po2017, 4L);
		// END Modul AFS
		
		// START Modul Programmieren 3
		Module prog3 = moduleFactory.createModule("Programmieren 3", po2017, 3L);
		// END Modul Programmieren 3
		
		// START Modul Datenbanksysteme
		Module dbs = moduleFactory.createModule("Datenbanksysteme", po2017, 3L);
		// END Modul Datenbanksysteme
		
		// START Modul Algorithmen und Datenstrukturen
		Module ads = moduleFactory.createModule("Algorithmen und Datenstrukturen", po2017, 3L);
		// END Modul Algorithmen und Datenstrukturen
		
		// START Einführung in die Medieninformatik
		Module eim = moduleFactory.createModule("Einführung in die Medieninformatik", po2017);
		// END Modul Einführung in die Medieninformatik
		
		// START Einführung in die Medieninformatik
		Module swt = moduleFactory.createModule("Softwaretechnik", po2017, 4L);
		// END Modul Einführung in die Medieninformatik
		
		// START ROOM D12
		Room d12 = roomFactory.createRoom("D12");
		var d12_out = roomRepository.save(d12);
		
		//Start ROOM D11
		Room d11 = roomFactory.createRoom("D11");
		var d11_out = roomRepository.save(d11);
		
		//Start ROOM D13
		Room d13 = roomFactory.createRoom("D13");
		var d13_out = roomRepository.save(d13);
		
		// START AFS Timeslots
		// START AFS VL
		Timeslot afsVorlesung = timeslotFactory.createTimeslotVorlesung(DayOfWeek.WEDNESDAY, afs, LocalTime.of(8, 15), d11_out);
		// END AFS VL
		
		// START AFS UEBUNG 1
		Timeslot afsUebung = timeslotFactory.createTimeslotUebung(DayOfWeek.WEDNESDAY, afs, LocalTime.of(10, 0), d12_out);
		// END AFS UEBUNG 1
		
		// START AFS UEBUNG 2
		Timeslot afsUebung2 = timeslotFactory.createTimeslotUebung(DayOfWeek.WEDNESDAY, afs, LocalTime.of(11, 45), d12_out);
		// END AFS UEBUNG 2
		
		// START AFS UEBUNG 3
		Timeslot afsUebung3 = timeslotFactory.createTimeslotUebung(DayOfWeek.MONDAY, afs, LocalTime.of(10, 0), d12_out);
		// END AFS UEBUNG 3
		
		// START AFS UEBUNG 4
		Timeslot afsUebung4 = timeslotFactory.createTimeslotUebung(DayOfWeek.FRIDAY, afs, LocalTime.of(11, 45), d12_out);
		// END AFS UEBUNG 4
		
		// START SWT Timeslots
		// START SWT VL
		Timeslot swtVorlesung = timeslotFactory.createTimeslotVorlesung(DayOfWeek.TUESDAY, swt, LocalTime.of(8, 15), d11_out);
		// END swt VL
		
		// START swt Prakt 1
		Timeslot swtPraktikum = timeslotFactory.createTimeslotPraktikum(DayOfWeek.TUESDAY, swt, LocalTime.of(10, 0), d12_out);
		// END swt Prakt 1
		
		// START swt Prakt 2
		Timeslot swtPraktikum2 = timeslotFactory.createTimeslotPraktikum(DayOfWeek.WEDNESDAY, swt, LocalTime.of(11, 45), d12_out);
		// END swt Prakt 2
		
		// START DBS Timeslots
		// START DBS VL
		Timeslot dbsVorlesung = timeslotFactory.createTimeslotVorlesung(DayOfWeek.FRIDAY, dbs, LocalTime.of(8, 15), d11_out);
		// END DBS VL
		
		// START DBS Prakt 1
		Timeslot dbsPraktikum = timeslotFactory.createTimeslotPraktikum(DayOfWeek.FRIDAY, dbs, LocalTime.of(10, 0), d13_out);
		// END DBS Prakt 1
		
		// START dbs Prakt 2
		Timeslot dbsPraktikum2 = timeslotFactory.createTimeslotPraktikum(DayOfWeek.FRIDAY, dbs, LocalTime.of(11, 45), d13_out);
		// END dbs Prakt 2
		
		
		// START PROG3 Timeslots
		// START PROG3 VL
		Timeslot prog3Vorlesung = timeslotFactory.createTimeslotVorlesung(DayOfWeek.MONDAY, prog3, LocalTime.of(8, 15), d11_out);
		// END PROG3 VL
		
		// START PROG3 Prakt 1
		Timeslot prog3Praktikum = timeslotFactory.createTimeslotPraktikum(DayOfWeek.MONDAY, prog3, LocalTime.of(10, 0), d12_out);
		// END PROG3 Prakt 1
		
		// START PROG3 Prakt 2
		Timeslot prog3Praktikum2 = timeslotFactory.createTimeslotPraktikum(DayOfWeek.MONDAY, prog3, LocalTime.of(11, 45), d13_out);
		// END PROG3 Prakt 2
		
		// START PROG3 Prakt 3
		Timeslot prog3Praktikum3 = timeslotFactory.createTimeslotPraktikum(DayOfWeek.MONDAY, prog3, LocalTime.of(10, 0), d13_out);
		// END PROG3 Prakt 3
		
		// START PROG3 Prakt 4
		Timeslot prog3Praktikum4 = timeslotFactory.createTimeslotPraktikum(DayOfWeek.MONDAY, prog3, LocalTime.of(11, 45), d12_out);
		// END PROG3 Prakt 4
		
		
		// START ADS Timeslots
		// START ADS VL
		Timeslot adsVorlesung = timeslotFactory.createTimeslotVorlesung(DayOfWeek.WEDNESDAY, ads, LocalTime.of(10, 00), d11_out);
		// END ads VL
		
		// START ADS Prakt 1
		Timeslot adsPraktikum = timeslotFactory.createTimeslotPraktikum(DayOfWeek.WEDNESDAY, ads, LocalTime.of(11, 30), d13_out);
		// END ads Prakt 1
		
		// START ads Prakt 2
		Timeslot adsPraktikum2 = timeslotFactory.createTimeslotPraktikum(DayOfWeek.WEDNESDAY, ads, LocalTime.of(14, 15), d13_out);
		// END ads Prakt 2
		
		
		// SAVE Lecturers first so attendees can reference them
		// KRECHEL
		var krechel_out = userRepository.save(krechel);
		afsUebung.setUser(krechel_out);
		afsUebung2.setUser(krechel_out);
		afsUebung3.setUser(krechel_out);
		afsUebung4.setUser(krechel_out);
		afsVorlesung.setUser(krechel_out);
		
		dbsVorlesung.setUser(krechel_out);
		dbsPraktikum.setUser(krechel_out);
		dbsPraktikum2.setUser(krechel_out);
		
		//WEITZ
		var weitz_out = userRepository.save(weitz);
		swtVorlesung.setUser(weitz_out);
		swtPraktikum.setUser(weitz_out);
		swtPraktikum2.setUser(weitz_out);
		
		prog3Vorlesung.setUser(weitz_out);
		prog3Praktikum.setUser(weitz_out);
		prog3Praktikum2.setUser(weitz_out);
		
		//FRITZ
		var fritz_out = userRepository.save(fritz);
		
		prog3Praktikum3.setUser(fritz_out);
		prog3Praktikum4.setUser(fritz_out);
		
		
		//Schwanecke
		var schwanecke_out = userRepository.save(schwanecke);
		
		adsVorlesung.setUser(schwanecke_out);
		adsPraktikum.setUser(schwanecke_out);
		adsPraktikum2.setUser(schwanecke_out);
		
		// SAVE TIMESLOTS TO ROOMS
		// D12
		List<Timeslot> toSaveD12 = new ArrayList<>();
		toSaveD12.add(afsUebung);
		toSaveD12.add(afsUebung2);
		toSaveD12.add(afsUebung3);
		toSaveD12.add(afsUebung4);
		toSaveD12.add(swtPraktikum);
		toSaveD12.add(swtPraktikum2);
		toSaveD12.add(prog3Praktikum);
		toSaveD12.add(prog3Praktikum4);
		
		d12_out.setTimeslots(toSaveD12);
		
		//D11
		List<Timeslot> toSaveD11 = new ArrayList<>();
		toSaveD11.add(afsVorlesung);
		toSaveD11.add(swtVorlesung);
		toSaveD11.add(dbsVorlesung);
		toSaveD11.add(prog3Vorlesung);
		toSaveD11.add(adsVorlesung);
		
		d11_out.setTimeslots(toSaveD11);
		
		//D13
		List<Timeslot> toSaveD13 = new ArrayList<>();
		toSaveD13.add(dbsPraktikum);
		toSaveD13.add(dbsPraktikum2);
		toSaveD13.add(prog3Praktikum2);
		toSaveD13.add(prog3Praktikum3);
		toSaveD13.add(adsPraktikum);
		toSaveD13.add(adsPraktikum2);
		
		d13_out.setTimeslots(toSaveD13);
		
		
		// START ADD TIMESLOTS DENNIS
		List<Timeslot> dennisTimeslots = new ArrayList<>();
		dennisTimeslots.add(afsUebung);
		dennisTimeslots.add(afsVorlesung);
		dennisTimeslots.add(dbsVorlesung);
		dennisTimeslots.add(dbsPraktikum2);
		dennisTimeslots.add(swtVorlesung);
		dennisTimeslots.add(swtPraktikum);
		dennisTimeslots.add(adsVorlesung);
		dennisTimeslots.add(adsPraktikum2);
		// END ADD TIMESLOTS DENNIS
		
		// START ADD TIMESLOTS CHANDLER
		List<Timeslot> chandlerTimeslots = new ArrayList<>();
		chandlerTimeslots.add(afsVorlesung);
		chandlerTimeslots.add(afsUebung2);
		chandlerTimeslots.add(dbsVorlesung);
		chandlerTimeslots.add(dbsPraktikum2);
		chandlerTimeslots.add(swtVorlesung);
		chandlerTimeslots.add(swtPraktikum);
		chandlerTimeslots.add(adsVorlesung);
		chandlerTimeslots.add(adsPraktikum2);
		chandlerTimeslots.add(prog3Vorlesung);
		chandlerTimeslots.add(prog3Praktikum3);
		// END ADD TIMESLOTS CHANDLER
		
		// START ADD TIMESLOTS WILLI
		List<Timeslot> williTimeslots = new ArrayList<>();
		williTimeslots.add(afsVorlesung);
		williTimeslots.add(afsUebung3);
		williTimeslots.add(dbsVorlesung);
		williTimeslots.add(dbsPraktikum2);
		williTimeslots.add(swtVorlesung);
		williTimeslots.add(swtPraktikum2);
		williTimeslots.add(adsVorlesung);
		williTimeslots.add(adsPraktikum2);
		williTimeslots.add(prog3Vorlesung);
		williTimeslots.add(prog3Praktikum2);
		// END ADD TIMESLOTS WILLI
		
		// START ADD TIMESLOTS JÖNDHARD
		List<Timeslot> joendhardTimeslots = new ArrayList<>();
		joendhardTimeslots.add(afsVorlesung);
		joendhardTimeslots.add(afsUebung4);
		joendhardTimeslots.add(dbsVorlesung);
		joendhardTimeslots.add(dbsPraktikum);
		joendhardTimeslots.add(swtVorlesung);
		joendhardTimeslots.add(swtPraktikum);
		joendhardTimeslots.add(adsVorlesung);
		joendhardTimeslots.add(adsPraktikum);
		joendhardTimeslots.add(prog3Vorlesung);
		joendhardTimeslots.add(prog3Praktikum);
		// END ADD TIMESLOTS JÖNDHARD
		
		// START ADD TIMESLOTS JÖNDHARD
		List<Timeslot> frodoTimeslots = new ArrayList<>();
		frodoTimeslots.add(afsVorlesung);
		frodoTimeslots.add(afsUebung4);
		frodoTimeslots.add(dbsVorlesung);
		frodoTimeslots.add(dbsPraktikum2);
		frodoTimeslots.add(swtVorlesung);
		frodoTimeslots.add(swtPraktikum2);
		frodoTimeslots.add(adsVorlesung);
		frodoTimeslots.add(adsPraktikum2);
		frodoTimeslots.add(prog3Vorlesung);
		frodoTimeslots.add(prog3Praktikum4);
		// END ADD TIMESLOTS JÖNDHARD
		
		
		// START ADD TIMESLOTS SAMWEIS
		List<Timeslot> samweisTimeslots = new ArrayList<>();
		samweisTimeslots.add(afsVorlesung);
		samweisTimeslots.add(afsUebung4);
		samweisTimeslots.add(dbsVorlesung);
		samweisTimeslots.add(dbsPraktikum2);
		samweisTimeslots.add(swtVorlesung);
		samweisTimeslots.add(swtPraktikum2);
		samweisTimeslots.add(adsVorlesung);
		samweisTimeslots.add(adsPraktikum2);
		samweisTimeslots.add(prog3Vorlesung);
		samweisTimeslots.add(prog3Praktikum4);
		// END ADD TIMESLOTS SAMWEIS
		
		
		// START ADD TIMESLOTS GANDALF
		List<Timeslot> gandalfTimeslots = new ArrayList<>();
		gandalfTimeslots.add(swtVorlesung);
		gandalfTimeslots.add(swtPraktikum);
		
		// START SET TIMESLOTS FOR USERS
		
		dennis.setTimeslots(dennisTimeslots);
		chandler.setTimeslots(chandlerTimeslots);
		joendhard.setTimeslots(joendhardTimeslots);
		willi.setTimeslots(williTimeslots);
		frodo.setTimeslots(frodoTimeslots);
		samweis.setTimeslots(samweisTimeslots);
		gandalf.setTimeslots(gandalfTimeslots);
		
		// END SET TIMESLOTS FOR USERS
		
		// START USERS LIST
		List<User> usersToSave = new ArrayList<>();
		usersToSave.add(dennis);
		usersToSave.add(chandler);
		usersToSave.add(joendhard);
		usersToSave.add(willi);
		usersToSave.add(frodo);
		usersToSave.add(samweis);
		usersToSave.add(gandalf);
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
		completedModulesDennis.add(eim);
		completedModulesDennis.add(prog3);
		dennis.setCompletedModules(completedModulesDennis);
		
	/*	List<Module> completedModulesGandalf = new ArrayList<>();
		completedModulesGandalf.add(ads);
		completedModulesGandalf.add(afs);
		completedModulesGandalf.add(prog3);
		completedModulesGandalf.add(dbs);
		gandalf.setCompletedModules(completedModulesGandalf);*/
		
		System.out.println(String.format("DENNIS WITH ID: %d", dennis.getId()));
		userRepository.saveAll(usersToSave); // saving both at the same time to prevent detached entity exception
		AdminSettings adminSettings = new AdminSettings();
		
		poRepository.save(po2017);
		
		adminSettings.setId(1);
		List<String> filters = new ArrayList<>();
		filters.add("COLLISION");
		//filters.add("CAPACITY");
		adminSettings.updateAdminSettings(true, filters);
		
		var persistedSettings = adminSettingsRepository.save(adminSettings);
		adminSettingsService.setAdminSettings(persistedSettings);
		
		log.info("--> users " + usersToSave);
		
		PO po2017_repo = poRepository.findByTitleIs(po2017.getTitle());
		ArrayList<User> students_repo = new ArrayList<>();
		for(User student : usersToSave) {
			User student_repo = userRepository.getOne(student.getId());
			students_repo.add(student_repo);
		}
		po2017_repo.setStudents(students_repo);
		
		poRepository.save(po2017_repo);
		
		log.info("Done saving timeTable...");
	}
	
	private PORestriction createRestriction(boolean isDual) {
		PORestriction poRestriction = new PORestriction();
		poRestriction.setByCP(new PORestriction.PORestrictionByCP());
		poRestriction.setBySemester(new PORestriction.PORestrictionBySemester());
		poRestriction.setByProgressiveRegulation(new PORestriction.PORestrictionByProgressiveRegulation());
		poRestriction.getByCP().setIsActive(true);
		poRestriction.getByCP().setMaxCP(40L);
		poRestriction.getByProgressiveRegulation().setIsActive(false);
		poRestriction.getBySemester().setIsActive(false);
		
		PORestriction.DualPO dualPO = new PORestriction.DualPO();
		dualPO.setIsActive(isDual);
		if(isDual) dualPO.setFreeDualDay(DayOfWeek.values()[new Random().nextInt(5)]);
		poRestriction.setDualPO(dualPO);
		
		return poRestriction;
	}
	
}
