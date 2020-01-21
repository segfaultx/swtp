package de.hsrm.mi.swtp.exchangeplatform.configuration.initiator;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import de.hsrm.mi.swtp.exchangeplatform.model.data.*;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.DayOfWeek;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.TypeOfTimeslots;
import de.hsrm.mi.swtp.exchangeplatform.model.factory.UserFactory;
import de.hsrm.mi.swtp.exchangeplatform.model.settings.AdminSettings;
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
	
	UserRepository userRepository;
	RoomRepository roomRepository;
	AdminSettingsService adminSettingsService;
	AdminSettingsRepository adminSettingsRepository;
	UserFactory userFactory;
	PORepository poRepository;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		
		log.info("Filling Database with dark magic");
		
		// START Dennis
		User dennis = UserFactory.createStudent("Dennis", "Schad", 1006351L);
		// END Dennis
		
		// START Willi
		User willi = UserFactory.createStudent("Willi", "Wusel", 1006555L);
		// END Willi
		
		// START Jöndhard
		User joendhard = UserFactory.createStudent("Jöndhard", "Joendhardson", 1006333L);
		// END Jöndhard
		
		// START Weitz
		User weitz = UserFactory.createLecturerADMIN("Wolfgang", "Weitz", 171717171717L);
		// END WEITZ
		
		// START FRITZ
		User fritz = UserFactory.createLecturer("Fritzchen", "Fritz", 16161616L);
		// END fritz
		
		// START Schwanecke
		User schwanecke = UserFactory.createLecturerADMIN("Ulrich", "Schwanecke", 16161616L);
		// END schwanecke
		
		// START CHANDLER
		User chandler = UserFactory.createStudent("Chandler", "Bing", 1005917L);
		// END CHANDLER
		
		// START FRODO
		User frodo = UserFactory.createStudent("Frodo", "Beutlin", 1035123L);
		// END FRODO
		
		// START GANDALF
		User gandalf = UserFactory.createStudent("Gandalf", "Der_Graue", 1035146L);
		// END GANDALF
		
		// START SAMWEIS
		User samweis = UserFactory.createStudent("Samweis", "Gamdschie", 1035233L);
		// END SAMWEIS
		
		// START KRECHEL
		User krechel = UserFactory.createLecturerADMIN("Dirk", "Krechel", 12345678L);
		// END KRECHEL
		
		// START PO 2017
		PORestriction restriction2017 = createRestriction(false);
		restriction2017.getByCP().setIsActive(true);
		restriction2017.getByCP().setMaxCP(40L);
		restriction2017.getByProgressiveRegulation().setIsActive(false);
		restriction2017.getDualPO().setIsActive(false);
		
		PO po2017 = new PO();
		po2017.setTitle("Medieninformatik PO17");
		po2017.setMajor("Medieninformatik");
		po2017.setValidSince(LocalDate.now().minusYears(3L));
		po2017.setDateEnd(LocalDate.now().plusYears(3L));
		po2017.setRestriction(restriction2017);
		// END PO 2017
		
		// START Modul AFS
		Module afs = new Module();
		afs.setName("Automaten und formale Sprachen");
		afs.setPo(po2017);
		afs.setSemester(3L);
		// END Modul AFS
		
		// START Modul Programmieren 3
		Module prog3 = new Module();
		prog3.setName("Programmieren 3");
		prog3.setPo(po2017);
		prog3.setSemester(3L);
		// END Modul Programmieren 3
		
		
		// START Modul Datenbanksysteme
		Module dbs = new Module();
		dbs.setName("Datenbanksysteme");
		dbs.setPo(po2017);
		dbs.setSemester(3L);
		// END Modul Datenbanksysteme
		
		// START Modul Algorithmen und Datenstrukturen
		Module ads = new Module();
		ads.setName("Algorithmen und Datenstrukturen");
		ads.setPo(po2017);
		ads.setSemester(3L);
		// END Modul Algorithmen und Datenstrukturen
		
		// START Einführung in die Medieninformatik
		Module eim = new Module();
		eim.setName("Einführung in die Medieninformatik");
		eim.setPo(po2017);
		eim.setSemester(1L);
		// END Modul Einführung in die Medieninformatik
		
		// START Einführung in die Medieninformatik
		Module swt = new Module();
		swt.setName("Softwaretechnik");
		swt.setPo(po2017);
		swt.setSemester(4L);
		// END Modul Einführung in die Medieninformatik
		
		// START ROOM D12
		Room d12 = new Room();
		d12.setLocation("Unter den Eichen");
		d12.setRoomNumber("D12");
		d12.setTimeslots(null);
		
		var d12_out = roomRepository.save(d12);
		
		//Start ROOM D11
		Room d11 = new Room();
		d11.setLocation("Unter den Eichen");
		d11.setRoomNumber("D11");
		d11.setTimeslots(null);
		
		var d11_out = roomRepository.save(d11);
		
		//Start ROOM D13
		Room d13 = new Room();
		d13.setLocation("Unter den Eichen");
		d13.setRoomNumber("D13");
		d13.setTimeslots(null);
		
		var d13_out = roomRepository.save(d13);
		
		// START AFS Timeslots
		// START AFS VL
		Timeslot afsVorlesung = new Timeslot();
		afsVorlesung.setTimeSlotType(TypeOfTimeslots.VORLESUNG);
		afsVorlesung.setCapacity(100);
		afsVorlesung.setModule(afs);
		afsVorlesung.setDay(DayOfWeek.WEDNESDAY);
		afsVorlesung.setTimeStart(LocalTime.of(8, 15));
		afsVorlesung.setTimeEnd(LocalTime.of(9, 45));
		
		afsVorlesung.setRoom(d11_out);
		afsVorlesung.setTimeTable(null);
		// END AFS VL
		
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
		
		// START SWT Timeslots
		// START SWT VL
		Timeslot swtVorlesung = new Timeslot();
		swtVorlesung.setTimeSlotType(TypeOfTimeslots.VORLESUNG);
		swtVorlesung.setCapacity(100);
		swtVorlesung.setModule(swt);
		swtVorlesung.setDay(DayOfWeek.TUESDAY);
		swtVorlesung.setTimeStart(LocalTime.of(8, 15));
		swtVorlesung.setTimeEnd(LocalTime.of(9, 45));
		
		swtVorlesung.setRoom(d11_out);
		swtVorlesung.setTimeTable(null);
		// END swt VL
		
		// START swt Prakt 1
		Timeslot swtPraktikum = new Timeslot();
		swtPraktikum.setTimeSlotType(TypeOfTimeslots.PRAKTIKUM);
		swtPraktikum.setCapacity(20);
		swtPraktikum.setModule(swt);
		swtPraktikum.setDay(DayOfWeek.TUESDAY);
		swtPraktikum.setTimeStart(LocalTime.of(10, 0));
		swtPraktikum.setTimeEnd(LocalTime.of(11, 30));
		
		swtPraktikum.setRoom(d12_out);
		swtPraktikum.setTimeTable(null);
		// END swt Prakt 1
		
		// START swt Prakt 2
		Timeslot swtPraktikum2 = new Timeslot();
		swtPraktikum2.setTimeSlotType(TypeOfTimeslots.PRAKTIKUM);
		swtPraktikum2.setCapacity(20);
		swtPraktikum2.setModule(swt);
		swtPraktikum2.setDay(DayOfWeek.WEDNESDAY);
		swtPraktikum2.setTimeStart(LocalTime.of(11, 45));
		swtPraktikum2.setTimeEnd(LocalTime.of(13, 15));
		
		swtPraktikum2.setRoom(d12_out);
		swtPraktikum2.setTimeTable(null);
		// END swt Prakt 2
		
		// START DBS Timeslots
		// START DBS VL
		Timeslot dbsVorlesung = new Timeslot();
		dbsVorlesung.setTimeSlotType(TypeOfTimeslots.VORLESUNG);
		dbsVorlesung.setCapacity(100);
		dbsVorlesung.setModule(dbs);
		dbsVorlesung.setDay(DayOfWeek.FRIDAY);
		dbsVorlesung.setTimeStart(LocalTime.of(8, 15));
		dbsVorlesung.setTimeEnd(LocalTime.of(9, 45));
		
		dbsVorlesung.setRoom(d11_out);
		dbsVorlesung.setTimeTable(null);
		// END DBS VL
		
		// START DBS Prakt 1
		Timeslot dbsPraktikum = new Timeslot();
		dbsPraktikum.setTimeSlotType(TypeOfTimeslots.PRAKTIKUM);
		dbsPraktikum.setCapacity(30);
		dbsPraktikum.setModule(dbs);
		dbsPraktikum.setDay(DayOfWeek.FRIDAY);
		dbsPraktikum.setTimeStart(LocalTime.of(10, 0));
		dbsPraktikum.setTimeEnd(LocalTime.of(11, 30));
		
		dbsPraktikum.setRoom(d13_out);
		dbsPraktikum.setTimeTable(null);
		// END DBS Prakt 1
		
		// START dbs Prakt 2
		Timeslot dbsPraktikum2 = new Timeslot();
		dbsPraktikum2.setTimeSlotType(TypeOfTimeslots.PRAKTIKUM);
		dbsPraktikum2.setCapacity(30);
		dbsPraktikum2.setModule(dbs);
		dbsPraktikum2.setDay(DayOfWeek.FRIDAY);
		dbsPraktikum2.setTimeStart(LocalTime.of(11, 45));
		dbsPraktikum2.setTimeEnd(LocalTime.of(13, 15));
		
		dbsPraktikum2.setRoom(d13_out);
		dbsPraktikum2.setTimeTable(null);
		// END dbs Prakt 2
		
		
		// START PROG3 Timeslots
		// START PROG3 VL
		Timeslot prog3Vorlesung = new Timeslot();
		prog3Vorlesung.setTimeSlotType(TypeOfTimeslots.VORLESUNG);
		prog3Vorlesung.setCapacity(100);
		prog3Vorlesung.setModule(prog3);
		prog3Vorlesung.setDay(DayOfWeek.MONDAY);
		prog3Vorlesung.setTimeStart(LocalTime.of(8, 15));
		prog3Vorlesung.setTimeEnd(LocalTime.of(9, 45));
		
		prog3Vorlesung.setRoom(d11_out);
		prog3Vorlesung.setTimeTable(null);
		// END PROG3 VL
		
		// START PROG3 Prakt 1
		Timeslot prog3Praktikum = new Timeslot();
		prog3Praktikum.setTimeSlotType(TypeOfTimeslots.PRAKTIKUM);
		prog3Praktikum.setCapacity(30);
		prog3Praktikum.setModule(prog3);
		prog3Praktikum.setDay(DayOfWeek.MONDAY);
		prog3Praktikum.setTimeStart(LocalTime.of(10, 0));
		prog3Praktikum.setTimeEnd(LocalTime.of(11, 30));
		
		prog3Praktikum.setRoom(d12_out);
		prog3Praktikum.setTimeTable(null);
		// END PROG3 Prakt 1
		
		// START PROG3 Prakt 2
		Timeslot prog3Praktikum2 = new Timeslot();
		prog3Praktikum2.setTimeSlotType(TypeOfTimeslots.PRAKTIKUM);
		prog3Praktikum2.setCapacity(30);
		prog3Praktikum2.setModule(prog3);
		prog3Praktikum2.setDay(DayOfWeek.MONDAY);
		prog3Praktikum2.setTimeStart(LocalTime.of(11, 45));
		prog3Praktikum2.setTimeEnd(LocalTime.of(13, 15));
		
		prog3Praktikum2.setRoom(d13_out);
		prog3Praktikum2.setTimeTable(null);
		// END PROG3 Prakt 2
		
		// START PROG3 Prakt 3
		Timeslot prog3Praktikum3 = new Timeslot();
		prog3Praktikum3.setTimeSlotType(TypeOfTimeslots.PRAKTIKUM);
		prog3Praktikum3.setCapacity(30);
		prog3Praktikum3.setModule(prog3);
		prog3Praktikum3.setDay(DayOfWeek.MONDAY);
		prog3Praktikum3.setTimeStart(LocalTime.of(10, 0));
		prog3Praktikum3.setTimeEnd(LocalTime.of(11, 30));
		
		prog3Praktikum3.setRoom(d13_out);
		prog3Praktikum3.setTimeTable(null);
		// END PROG3 Prakt 3
		
		// START PROG3 Prakt 4
		Timeslot prog3Praktikum4 = new Timeslot();
		prog3Praktikum4.setTimeSlotType(TypeOfTimeslots.PRAKTIKUM);
		prog3Praktikum4.setCapacity(30);
		prog3Praktikum4.setModule(prog3);
		prog3Praktikum4.setDay(DayOfWeek.MONDAY);
		prog3Praktikum4.setTimeStart(LocalTime.of(11, 45));
		prog3Praktikum4.setTimeEnd(LocalTime.of(13, 15));
		
		prog3Praktikum4.setRoom(d12_out);
		prog3Praktikum4.setTimeTable(null);
		// END PROG3 Prakt 4
		
		
		// START ADS Timeslots
		// START ADS VL
		Timeslot adsVorlesung = new Timeslot();
		adsVorlesung.setTimeSlotType(TypeOfTimeslots.VORLESUNG);
		adsVorlesung.setCapacity(100);
		adsVorlesung.setModule(ads);
		adsVorlesung.setDay(DayOfWeek.WEDNESDAY);
		adsVorlesung.setTimeStart(LocalTime.of(10, 00));
		adsVorlesung.setTimeEnd(LocalTime.of(11, 30));
		
		adsVorlesung.setRoom(d11_out);
		adsVorlesung.setTimeTable(null);
		// END ads VL
		
		// START ADS Prakt 1
		Timeslot adsPraktikum = new Timeslot();
		adsPraktikum.setTimeSlotType(TypeOfTimeslots.PRAKTIKUM);
		adsPraktikum.setCapacity(30);
		adsPraktikum.setModule(ads);
		adsPraktikum.setDay(DayOfWeek.WEDNESDAY);
		adsPraktikum.setTimeStart(LocalTime.of(11, 30));
		adsPraktikum.setTimeEnd(LocalTime.of(13, 15));
		
		adsPraktikum.setRoom(d13_out);
		adsPraktikum.setTimeTable(null);
		// END ads Prakt 1
		
		// START ads Prakt 2
		Timeslot adsPraktikum2 = new Timeslot();
		adsPraktikum2.setTimeSlotType(TypeOfTimeslots.PRAKTIKUM);
		adsPraktikum2.setCapacity(30);
		adsPraktikum2.setModule(ads);
		adsPraktikum2.setDay(DayOfWeek.WEDNESDAY);
		adsPraktikum2.setTimeStart(LocalTime.of(14, 15));
		adsPraktikum2.setTimeEnd(LocalTime.of(15, 45));
		
		adsPraktikum2.setRoom(d13_out);
		adsPraktikum2.setTimeTable(null);
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
		filters.add("CAPACITY");
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
