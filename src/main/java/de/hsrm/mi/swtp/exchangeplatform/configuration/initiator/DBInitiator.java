package de.hsrm.mi.swtp.exchangeplatform.configuration.initiator;

import de.hsrm.mi.swtp.exchangeplatform.model.admin.po.enums.ProgressiveRegulationSpan;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import de.hsrm.mi.swtp.exchangeplatform.model.data.*;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.DayOfWeek;
import de.hsrm.mi.swtp.exchangeplatform.model.factory.*;
import de.hsrm.mi.swtp.exchangeplatform.model.admin.settings.AdminSettings;
import de.hsrm.mi.swtp.exchangeplatform.repository.*;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.TradeFilter.CustomPythonFilter;
import de.hsrm.mi.swtp.exchangeplatform.service.settings.AdminSettingsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
	TradeOfferRepository tradeOfferRepository;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		
		log.info("Filling Database with dark magic");
		
		//START STUDENTS
		// START Dennis
		User dennis = userFactory.createStudent("Dennis", "Schad", 1006351L, 9L);
		// END Dennis
		
		// START Willi
		User willi = userFactory.createStudent("Willi", "Wusel", 1006555L, 7L);
		// END Willi
		
		// START Jöndhard
		User joendhard = userFactory.createStudent("Jöndhard", "Joendhardson", 1006333L, 3L);
		// END Jöndhard
		
		// START CHANDLER
		User chandler = userFactory.createStudent("Chandler", "Bing", 1005917L, 5L);
		// END CHANDLER
		
		// START FRODO
		User frodo = userFactory.createStudent("Frodo", "Beutlin", 1035123L, 5L);
		// END FRODO
		
		// START GANDALF
		User gandalf = userFactory.createStudent("Gandalf", "Der_Graue", 1035146L, 7L);
		// END GANDALF
		
		// START SAMWEIS
		User samweis = userFactory.createStudent("Samweis", "Gamdschie", 1035233L, 7L);
		// END SAMWEIS
		//END STUDENTS
		
		
		//START LECTURERS
		// START FRITZ
		User fritz = userFactory.createLecturer("Fritzchen", "Fritz", 16161616L, "FF");
		// END fritz
		
		//START CALVUS
		User calvus = userFactory.createLecturer("Daniela", "Calvus", 87654321L, "DC");
		//END CALVUS
		
		//START BRECKHEIMER
		User breckheimer = userFactory.createLecturer("Stephan", "Breckheimer", 100000L, "DC");
		//END BRECKHEIMER
		
		//START EDERER
		User ederer = userFactory.createLecturer("Peter", "Ederer", 100000L, "PE");
		//END EDERER
		
		//START REICHENHAUER
		User reichenhauer = userFactory.createLecturer("Roland", "Reichenhauer", 10156400L, "PE");
		//END REICHENHAUER
		
		//START HUENEMOHR
		User huenemohr = userFactory.createLecturer("Holger", "Huenemohr", 198746L, "PE");
		//END HUENEMOHR
		//END LECTURERS
		
		
		//START LECTURER + ADMINS
		// START Weitz
		User weitz = userFactory.createLecturerADMIN("Wolfgang", "Weitz", 171717171717L, "WZ");
		// END WEITZ
		
		// START Schwanecke
		User schwanecke = userFactory.createLecturerADMIN("Ulrich", "Schwanecke", 16161616L, "SW");
		// END schwanecke
		
		// START KRECHEL
		User krechel = userFactory.createLecturerADMIN("Dirk", "Krechel", 12345678L, "KC");
		// END KRECHEL
		
		//START BERDUX
		User berdux = userFactory.createLecturerADMIN("Joerg", "Berdux", 42424242L, "BX");
		//END BERDUX
		
		//START SCHAIBLE
		User schaible = userFactory.createLecturerADMIN("Philipp", "Schaible", 5984131L, "PS");
		//END SCHAIBLE
		
		//START Pedersen
		User pedersen = userFactory.createLecturerADMIN("Sebastian", "Pedersen", 1212122L, "PD");
		//END Pedersen
		//END LECTURER + ADMINS
		
		
		// START PO 2017
		PORestriction restriction2017 = createRestriction(false);
		PO po2017 = poFactory.createPO(LocalDate.now().minusYears(3L), LocalDate.now().minusYears(3L), LocalDate.now().plusYears(3L));
		po2017.setRestriction(restriction2017);
		
		PORestriction restriction2017Dual = poRestrictionFactory.createRestriction();
		PO po2017Dual = poFactory.createPO("PO 2017 Dual", "Medieninformatik", (long) 7, LocalDate.now().minusYears(3L), LocalDate.now().minusYears(3L), LocalDate.now().plusYears(3L));
		
		final List<DayOfWeek> freeDaysPerSemester = IntStream.iterate(0, i -> i < po2017.getSemesterCount(), i -> i + 1)
															 .mapToObj(i -> DayOfWeek.TUESDAY)
															 .collect(Collectors.toList());
		restriction2017Dual.getDualPO().setFreeDualDays(freeDaysPerSemester);
		po2017Dual.setRestriction(restriction2017Dual);
		
		// END PO 2017
		
		//START SEMESTER 1
		// START Modul Einführung in die Medieninformatik
		Module eim = moduleFactory.createModule("Einführung in die Medieninformatik", "MI", 1100L, po2017, 1L);
		// END Modul Einführung in die Medieninformatik
		
		// START Modul Programmieren 1
		Module prog1 = moduleFactory.createModule("Programmieren 1", "PROG 1", 1120L, po2017, 1L);
		// END Modul Programmieren 1
		
		// START Modul Einfuerung in die Gestaltung
		Module eges = moduleFactory.createModule("Einfuerung in die Gestaltung", "EGES", 1230L, po2017, 1L);
		// END Modul Einfuerung in die Gestaltung
		
		// START Modul Analysis
		Module ana = moduleFactory.createModule("Analysis", "ANA", 1340L, po2017, 1L);
		// END Modul Analysis
		
		// START Modul Grundlagen der Betriebswirtschaftslehre
		Module bwl = moduleFactory.createModule("Grundlagen der Betriebswirtschaftslehre", "BWL", 1450L, po2017, 1L);
		// END Modul Grundlagen der Betriebswirtschaftslehre
		//END SEMESTER 1
		
		
		//START SEMESTER 2
		// START Modul Algorithmen und Datenstrukturen
		Module ads = moduleFactory.createModule("Algorithmen und Datenstrukturen", "ADS", 2110L, po2017, 2L);
		// END Modul Algorithmen und Datenstrukturen
		
		// START Modul Auszeichnungssprachen
		Module azs = moduleFactory.createModule("Auszeichnungssprachen", "AZS", 2120L, po2017, 2L);
		// END Modul Auszeichnungssprachen
		
		// START Modul Programmieren 2
		Module prog2 = moduleFactory.createModule("Programmieren 2", "PROG 2", 2130L, po2017, 2L);
		// END Modul Programmieren 2
		
		// START Modul Gestaltung elektronischer Medien
		Module gem = moduleFactory.createModule("Gestaltung elektronischer Medien", "GEM", 2240L, po2017, 2L);
		// END Modul Gestaltung elektronischer Medien
		
		// START Lineare Algebra
		Module la = moduleFactory.createModule("Lineare Algebra", "LA", 2350L, po2017, 2L);
		// END Modul Lineare Algebra
		
		// START IT-Recht und Datenschutz
		Module recht = moduleFactory.createModule("IT-Recht und Datenschutz", "RECHT", 2460L, po2017, 2L);
		// END Modul IT-Recht und Datenschutz
		//END SEMESTER 2
		
		
		//START SEMESTER 3
		// START Modul AFS
		Module afs = moduleFactory.createModule("Automaten und formale Sprachen", "AFS", 3110L, po2017, 3L);
		// END Modul AFS
		
		// START Modul Datenbanksysteme
		Module dbs = moduleFactory.createModule("Datenbanksysteme", "DBS", 3120L, po2017, 3L);
		// END Modul Datenbanksysteme
		
		// START Modul Entwicklung interaktiver Benutzungsoberflaechen
		Module eibo = moduleFactory.createModule("Entwicklung interaktiver Benutzungsoberflaechen", "EIBO", 3120L, po2017, 3L);
		// END Modul Entwicklung interaktiver Benutzungsoberflaechen
		
		// START Modul Programmieren 3
		Module prog3 = moduleFactory.createModule("Programmieren 3", "Prog 3", 3140L, po2017, 3L);
		// END Modul Programmieren 3
		
		// START Modul Angewandte Mathematik
		Module am = moduleFactory.createModule("Angewandte Mathematik", "AM", 3360L, po2017, 3L);
		// END Modul Angewandte Mathematik
		//END SEMESTER 3
		
		
		//START SEMESTER 4
		// START Modul CG
		Module cg = moduleFactory.createModule("Computergrafik", "CG", 4120L, po2017, 4L);
		// END Modul CG
		
		// START Modul Rechnernetze und Betriebssysteme
		Module rn = moduleFactory.createModule("Rechnernetze und Betriebssysteme", "RN", 4130L, po2017, 4L);
		// END Modul Rechnernetze und Betriebssysteme
		
		// START Modul Softwaretechnik
		Module swt = moduleFactory.createModule("Softwaretechnik", "SWT", 4140L, po2017, 4L);
		// END Modul Softwaretechnik
		
		// START Modul Webbasierte Anwendungen
		Module web = moduleFactory.createModule("Webbasierte Anwendungen", "WEB", 4150L, po2017, 4L);
		// END Modul Webbasierte Anwendungen
		
		// START Modul Animation
		Module ani = moduleFactory.createModule("Animation", "ANI", 4250L, po2017, 4L);
		// END Modul Animation
		//END SEMESTER 4
		
		
		
		// START ROOM D12
		Room d12 = roomFactory.createRoom("D12");
		var d12_out = roomRepository.save(d12);
		
		//Start ROOM D11
		Room d11 = roomFactory.createRoom("D11");
		var d11_out = roomRepository.save(d11);
		
		//Start ROOM D13
		Room d13 = roomFactory.createRoom("D13");
		var d13_out = roomRepository.save(d13);
		
		//Start ROOM D14
		Room d14 = roomFactory.createRoom("D14");
		var d14_out = roomRepository.save(d14);
		
		//START TIMESLOTS SEMESTER 1
		
		//START MI Timeslots
		//START MI VL
		Timeslot miVorlesung = timeslotFactory.createTimeslotVorlesung(DayOfWeek.MONDAY, eim, LocalTime.of(11, 45), d11_out);
		//END MI VL
		// START MI Prakt 1
		Timeslot emiPraktikum = timeslotFactory.createTimeslotPraktikum(DayOfWeek.MONDAY,  "A", eim, LocalTime.of(14, 15), d13_out);
		// END MI Prakt 1
		// START MI Prakt 2
		Timeslot emiPraktikum2 = timeslotFactory.createTimeslotPraktikum(DayOfWeek.WEDNESDAY, "B", eim, LocalTime.of(10, 0), d13_out);
		// END MI Prakt 2
		//END MI Timeslots
		
		//START PROG 1 Timeslots
		//START PROG 1 VL
		Timeslot prog1Vorlesung = timeslotFactory.createTimeslotVorlesung(DayOfWeek.THURSDAY, prog1, LocalTime.of(10, 0), d11_out);
		//END PROG 1 VL
		// START PROG1 Prakt 1
		Timeslot prog1Praktikum = timeslotFactory.createTimeslotPraktikum(DayOfWeek.THURSDAY,  "A", prog1, LocalTime.of(11, 45), d13_out);
		// END PROG1 Prakt 1
		// START PROG1 Prakt 2
		Timeslot prog1Praktikum1 = timeslotFactory.createTimeslotPraktikum(DayOfWeek.THURSDAY,  "B", prog1, LocalTime.of(14, 15), d13_out);
		// END PROG1 Prakt 2
		// START PROG1 Prakt 3
		Timeslot prog1Praktikum2 = timeslotFactory.createTimeslotPraktikum(DayOfWeek.THURSDAY,  "C", prog1, LocalTime.of(16, 0), d13_out);
		// END PROG1 Prakt 3
		// START PROG1 Prakt 4
		Timeslot prog1Praktikum3 = timeslotFactory.createTimeslotPraktikum(DayOfWeek.WEDNESDAY,  "D", prog1, LocalTime.of(14, 15), d13_out);
		// END PROG1 Prakt 4
		//END PROG 1 Timeslots
		
		//START ANALYSIS Timeslots
		//START ANALYSIS VL
		Timeslot analysisVorlesung = timeslotFactory.createTimeslotVorlesung(DayOfWeek.WEDNESDAY, ana, LocalTime.of(8, 15), d11_out);
		//END ANALYSIS VL
		// START ANALYSIS UEBUNG 1
		Timeslot analysisUebung = timeslotFactory.createTimeslotUebung(DayOfWeek.WEDNESDAY,  "I", ana, LocalTime.of(10, 0), d11_out);
		// END ANALYSIS UEBUNG 1
		// START ANALYSIS UEBUNG 2
		Timeslot analysisUebung1 = timeslotFactory.createTimeslotUebung(DayOfWeek.WEDNESDAY,  "II", prog1, LocalTime.of(11, 45), d11_out);
		// END ANALYSIS UEBUNG 2
		//END ANALYSIS Timeslots
		
		//START BWL Timeslots
		//START BWL VL
		Timeslot bwlVorlesung = timeslotFactory.createTimeslotVorlesung(DayOfWeek.FRIDAY, bwl, LocalTime.of(10, 10), d11_out);
		//END BWL VL
		// START BWL UEBUNG 1
		Timeslot bwlUebung = timeslotFactory.createTimeslotUebung(DayOfWeek.FRIDAY,  "I", bwl, LocalTime.of(11, 45), d11_out);
		// END BWL UEBUNG 1
		// START BWL UEBUNG 2
		Timeslot bwlUebung1 = timeslotFactory.createTimeslotUebung(DayOfWeek.FRIDAY,  "II", bwl, LocalTime.of(14, 15), d11_out);
		// END BWL UEBUNG 2
		//END BWL Timeslots
		
		//START GESTALTUNG Timeslots
		//START GESTALTUNG VL
		Timeslot gestaltungVorlesung = timeslotFactory.createTimeslotVorlesung(DayOfWeek.TUESDAY, eges, LocalTime.of(10, 0), d11_out);
		//END GESTALTUNG VL
		// START GESTALTUNG Prakt 1
		Timeslot gestaltungPraktikum = timeslotFactory.createTimeslotPraktikum(DayOfWeek.TUESDAY,  "A", eges, LocalTime.of(11, 45), d13_out);
		// END GESTALTUNG Prakt 1
		// START GESTALTUNG Prakt 2
		Timeslot gestaltungPraktikum1 = timeslotFactory.createTimeslotPraktikum(DayOfWeek.TUESDAY,  "B", eges, LocalTime.of(14, 15), d13_out);
		// END GESTALTUNG Prakt 2
		// START GESTALTUNG Prakt 3
		Timeslot gestaltungPraktikum2 = timeslotFactory.createTimeslotPraktikum(DayOfWeek.TUESDAY,  "C", eges, LocalTime.of(16, 0), d13_out);
		// END GESTALTUNG Prakt 3
		// START GESTALTUNG Prakt 4
		Timeslot gestaltungPraktikum3 = timeslotFactory.createTimeslotPraktikum(DayOfWeek.TUESDAY,  "D", eges, LocalTime.of(14, 15), d13_out);
		// END GESTALTUNG Prakt 4
		//END GESTALTUNG 1 Timeslots
		
		//END TIMESLOTS SEMESTER 1
		
		
		//START TIMESLOTS SEMESTER 2
		
		// START ADS Timeslots
		// START ADS VL
		Timeslot adsVorlesung = timeslotFactory.createTimeslotVorlesung(DayOfWeek.WEDNESDAY, ads, LocalTime.of(10, 0), d11_out);
		// END ADS VL
		// START ADS Prakt 1
		Timeslot adsPraktikum = timeslotFactory.createTimeslotPraktikum(DayOfWeek.WEDNESDAY,  "A", ads, LocalTime.of(11, 30), d13_out);
		// END ADS Prakt 1
		// START ADS Prakt 2
		Timeslot adsPraktikum2 = timeslotFactory.createTimeslotPraktikum(DayOfWeek.TUESDAY,"B", ads, LocalTime.of(14, 15), d13_out);
		// END ADS Prakt 2
		// START ADS Prakt 1
		Timeslot adsPraktikum3 = timeslotFactory.createTimeslotPraktikum(DayOfWeek.TUESDAY,  "A", ads, LocalTime.of(14, 15), d13_out);
		// END ADS Prakt 1
		// START ADS Prakt 2
		Timeslot adsPraktikum4 = timeslotFactory.createTimeslotPraktikum(DayOfWeek.WEDNESDAY,"B", ads, LocalTime.of(14, 15), d13_out);
		// END ADS Prakt 2
		//END ADS Timeslots
		
		// START AUSZEICHNUNGSSPRACHEN Timeslots
		// START AUSZEICHNUNGSSPRACHEN VL
		Timeslot auszeichnungsVorlesung = timeslotFactory.createTimeslotVorlesung(DayOfWeek.WEDNESDAY, azs, LocalTime.of(14, 15), d11_out);
		// END AUSZEICHNUNGSSPRACHEN VL
		// START AUSZEICHNUNGSSPRACHEN Prakt 1
		Timeslot auszeichnungsPraktikum = timeslotFactory.createTimeslotPraktikum(DayOfWeek.MONDAY,  "A", azs, LocalTime.of(11, 30), d13_out);
		// END AUSZEICHNUNGSSPRACHEN Prakt 1
		// START AUSZEICHNUNGSSPRACHEN Prakt 2
		Timeslot auszeichnungsPraktikum2 = timeslotFactory.createTimeslotPraktikum(DayOfWeek.MONDAY,"B", azs, LocalTime.of(11, 30), d13_out);
		// END AUSZEICHNUNGSSPRACHEN Prakt 2
		// START AUSZEICHNUNGSSPRACHEN Prakt 1
		Timeslot auszeichnungsPraktikum3 = timeslotFactory.createTimeslotPraktikum(DayOfWeek.TUESDAY,  "A", azs, LocalTime.of(14, 15), d13_out);
		// END AUSZEICHNUNGSSPRACHEN Prakt 1
		// START AUSZEICHNUNGSSPRACHEN Prakt 2
		Timeslot auszeichnungsPraktikum4 = timeslotFactory.createTimeslotPraktikum(DayOfWeek.WEDNESDAY,"B", azs, LocalTime.of(14, 15), d13_out);
		// END AUSZEICHNUNGSSPRACHEN Prakt 2
		//END AUSZEICHNUNGSSPRACHEN Timeslots
		
		//START LA Timeslots
		//START LA VL
		Timeslot laVorlesung = timeslotFactory.createTimeslotVorlesung(DayOfWeek.WEDNESDAY, la, LocalTime.of(8, 15), d11_out);
		//END LA VL
		// START LA UEBUNG 1
		Timeslot laUebung = timeslotFactory.createTimeslotUebung(DayOfWeek.WEDNESDAY,  "I", la, LocalTime.of(10, 0), d11_out);
		// END LA UEBUNG 1
		// START LA UEBUNG 2
		Timeslot laUebung1 = timeslotFactory.createTimeslotUebung(DayOfWeek.WEDNESDAY,  "II", la, LocalTime.of(11, 45), d11_out);
		// END LA UEBUNG 2
		//END LA Timeslots
		
		//START RECHT Timeslots
		//START RECHT VL
		Timeslot rechtVorlesung = timeslotFactory.createTimeslotVorlesung(DayOfWeek.FRIDAY, recht, LocalTime.of(11, 45), d11_out);
		//END RECHT VL
		// START RECHT UEBUNG 1
		Timeslot rechtUebung = timeslotFactory.createTimeslotUebung(DayOfWeek.FRIDAY,  "I", recht, LocalTime.of(14, 15), d11_out);
		// END RECHT UEBUNG 1
		// START RECHT UEBUNG 2
		Timeslot rechtUebung1 = timeslotFactory.createTimeslotUebung(DayOfWeek.FRIDAY,  "II", recht, LocalTime.of(16, 0), d11_out);
		// END RECHT UEBUNG 2
		//END RECHT Timeslots
		
		// START GEM Timeslots
		// START GEM VL
		Timeslot gemVorlesung = timeslotFactory.createTimeslotVorlesung(DayOfWeek.MONDAY, gem, LocalTime.of(8, 15), d11_out);
		// END GEM VL
		// START GEM Prakt 1
		Timeslot gemPraktikum = timeslotFactory.createTimeslotPraktikum(DayOfWeek.MONDAY,  "A", gem, LocalTime.of(10, 0), d13_out);
		// END GEM Prakt 1
		// START GEM Prakt 2
		Timeslot gemPraktikum2 = timeslotFactory.createTimeslotPraktikum(DayOfWeek.MONDAY,"B", gem, LocalTime.of(10, 0), d13_out);
		// END GEM Prakt 2
		// START GEM Prakt 1
		Timeslot gemPraktikum3 = timeslotFactory.createTimeslotPraktikum(DayOfWeek.TUESDAY,  "A", gem, LocalTime.of(14, 15), d13_out);
		// END GEM Prakt 1
		// START GEM Prakt 2
		Timeslot gemPraktikum4 = timeslotFactory.createTimeslotPraktikum(DayOfWeek.TUESDAY,"B", gem, LocalTime.of(14, 15), d13_out);
		// END GEM Prakt 2
		//END GEM Timeslots
		
		// START PROG2 Timeslots
		// START PROG2 VL
		Timeslot prog2Vorlesung = timeslotFactory.createTimeslotVorlesung(DayOfWeek.THURSDAY, prog2, LocalTime.of(8, 15), d11_out);
		// END PROG2 VL
		// START PROG2 Prakt 1
		Timeslot prog2Praktikum = timeslotFactory.createTimeslotPraktikum(DayOfWeek.THURSDAY,  "A", prog2, LocalTime.of(10, 0), d13_out);
		// END PROG2 Prakt 1
		// START PROG2 Prakt 2
		Timeslot prog2Praktikum2 = timeslotFactory.createTimeslotPraktikum(DayOfWeek.THURSDAY,"B", prog2, LocalTime.of(11, 45), d13_out);
		// END PROG2 Prakt 2
		// START PROG2 Prakt 1
		Timeslot prog2Praktikum3 = timeslotFactory.createTimeslotPraktikum(DayOfWeek.THURSDAY,  "A", prog2, LocalTime.of(14, 15), d13_out);
		// END PROG2 Prakt 1
		// START PROG2 Prakt 2
		Timeslot prog2Praktikum4 = timeslotFactory.createTimeslotPraktikum(DayOfWeek.THURSDAY,"B", prog2, LocalTime.of(16, 0), d13_out);
		// END PROG2 Prakt 2
		//END PROG2 Timeslots
		//END TIMESLOTS SEMESTER 2
		
		
		//START TIMESLOTS SEMESTER 3
		
		// START PROG3 Timeslots
		// START PROG3 VL
		Timeslot prog3Vorlesung = timeslotFactory.createTimeslotVorlesung(DayOfWeek.MONDAY, prog3, LocalTime.of(8, 15), d11_out);
		// END PROG3 VL
		// START PROG3 Prakt 1
		Timeslot prog3Praktikum = timeslotFactory.createTimeslotPraktikum(DayOfWeek.MONDAY,  "A", prog3, LocalTime.of(10, 0), d12_out);
		// END PROG3 Prakt 1
		// START PROG3 Prakt 2
		Timeslot prog3Praktikum2 = timeslotFactory.createTimeslotPraktikum(DayOfWeek.MONDAY,  "B", prog3, LocalTime.of(11, 45), d13_out);
		// END PROG3 Prakt 2
		// START PROG3 Prakt 3
		Timeslot prog3Praktikum3 = timeslotFactory.createTimeslotPraktikum(DayOfWeek.MONDAY,  "C", prog3, LocalTime.of(10, 0), d13_out);
		// END PROG3 Prakt 3
		// START PROG3 Prakt 4
		Timeslot prog3Praktikum4 = timeslotFactory.createTimeslotPraktikum(DayOfWeek.MONDAY,  "D", prog3, LocalTime.of(11, 45), d12_out);
		// END PROG3 Prakt 4
		//END PROG3 Timeslots
		
		// START AFS Timeslots
		// START AFS VL
		Timeslot afsVorlesung = timeslotFactory.createTimeslotVorlesung(DayOfWeek.WEDNESDAY, afs, LocalTime.of(8, 15), d11_out);
		// END AFS VL
		// START AFS UEBUNG 1
		Timeslot afsUebung = timeslotFactory.createTimeslotUebung(DayOfWeek.WEDNESDAY, "I",  afs, LocalTime.of(10, 0), d12_out);
		// END AFS UEBUNG 1
		// START AFS UEBUNG 2
		Timeslot afsUebung2 = timeslotFactory.createTimeslotUebung(DayOfWeek.WEDNESDAY, "II", afs, LocalTime.of(11, 45), d12_out);
		// END AFS UEBUNG 2
		// START AFS UEBUNG 3
		Timeslot afsUebung3 = timeslotFactory.createTimeslotUebung(DayOfWeek.MONDAY,  "III", afs, LocalTime.of(10, 0), d12_out);
		// END AFS UEBUNG 3
		// START AFS UEBUNG 4
		Timeslot afsUebung4 = timeslotFactory.createTimeslotUebung(DayOfWeek.FRIDAY, "IV", afs, LocalTime.of(11, 45), d12_out);
		// END AFS UEBUNG 4
		//END AFS Timeslots
		
		// START DBS Timeslots
		// START DBS VL
		Timeslot dbsVorlesung = timeslotFactory.createTimeslotVorlesung(DayOfWeek.FRIDAY, dbs, LocalTime.of(8, 15), d11_out);
		// END DBS VL
		// START DBS Prakt 1
		Timeslot dbsPraktikum = timeslotFactory.createTimeslotPraktikum(DayOfWeek.FRIDAY,  "A", dbs, LocalTime.of(10, 0), d13_out);
		// END DBS Prakt 1
		// START dbs Prakt 2
		Timeslot dbsPraktikum2 = timeslotFactory.createTimeslotPraktikum(DayOfWeek.FRIDAY,  "B", dbs, LocalTime.of(11, 45), d13_out);
		// END dbs Prakt 2
		//END DBS Timeslots
		
		// START EIBO Timeslots
		// START EIBO VL
		Timeslot eiboVorlesung = timeslotFactory.createTimeslotVorlesung(DayOfWeek.FRIDAY, eibo, LocalTime.of(8, 15), d11_out);
		// END EIBO VL
		// START EIBO Prakt 1
		Timeslot eiboPraktikum = timeslotFactory.createTimeslotPraktikum(DayOfWeek.FRIDAY,  "A", eibo, LocalTime.of(10, 0), d13_out);
		// END EIBO Prakt 1
		// START EIBO Prakt 2
		Timeslot eiboPraktikum2 = timeslotFactory.createTimeslotPraktikum(DayOfWeek.FRIDAY,  "B", eibo, LocalTime.of(11, 45), d13_out);
		// END EIBO Prakt 2
		// START EIBO Prakt 3
		Timeslot eiboPraktikum3 = timeslotFactory.createTimeslotPraktikum(DayOfWeek.MONDAY,  "C", eibo, LocalTime.of(14, 15), d13_out);
		// END EIBO Prakt 3
		// START EIBO Prakt 4
		Timeslot eiboPraktikum4 = timeslotFactory.createTimeslotPraktikum(DayOfWeek.MONDAY,  "D", eibo, LocalTime.of(16, 0), d12_out);
		// END EIBO Prakt 4
		//END EIBO Timeslots
		
		//START MATHE3 Timeslots
		//START MATHE3 VL
		Timeslot amVorlesung = timeslotFactory.createTimeslotVorlesung(DayOfWeek.WEDNESDAY, am, LocalTime.of(8, 15), d11_out);
		//END MATHE3 VL
		// START MATHE3 UEBUNG 1
		Timeslot amUebung = timeslotFactory.createTimeslotUebung(DayOfWeek.WEDNESDAY,  "I", am, LocalTime.of(10, 0), d11_out);
		// END MATHE3 UEBUNG 1
		// START MATHE3 UEBUNG 2
		Timeslot amUebung1 = timeslotFactory.createTimeslotUebung(DayOfWeek.WEDNESDAY,  "II", am, LocalTime.of(11, 45), d11_out);
		// END MATHE3 UEBUNG 2
		//END MATHE3 Timeslots
		
		//END TIMESLOTS SEMESTER 3
		
		
		//START TIMESLOTS SEMESTER 4
		
		// START SWT Timeslots
		// START SWT VL
		Timeslot swtVorlesung = timeslotFactory.createTimeslotVorlesung(DayOfWeek.TUESDAY, swt, LocalTime.of(8, 15), d11_out);
		// END swt VL
		// START swt Prakt 1
		Timeslot swtPraktikum = timeslotFactory.createTimeslotPraktikum(DayOfWeek.TUESDAY,  "A", swt, LocalTime.of(10, 0), d12_out);
		// END swt Prakt 1
		// START swt Prakt 2
		Timeslot swtPraktikum2 = timeslotFactory.createTimeslotPraktikum(DayOfWeek.WEDNESDAY,  "B", swt, LocalTime.of(11, 45), d12_out);
		// END swt Prakt 2
		//END SWT Timeslots
		
		//END TIMESLOTS SEMESTER 4
		
		
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
		
		//REICHENHAUER
		var reichenhauer_out = userRepository.save(reichenhauer);
		
		analysisVorlesung.setUser(reichenhauer_out);
		analysisUebung.setUser(reichenhauer_out);
		analysisUebung1.setUser(reichenhauer_out);
		
		laVorlesung.setUser(reichenhauer_out);
		laUebung.setUser(reichenhauer_out);
		laUebung1.setUser(reichenhauer_out);
		
		//BRECKHEIMER
		var breckheimer_out = userRepository.save(breckheimer);
		
		rechtVorlesung.setUser(breckheimer_out);
		rechtUebung.setUser(breckheimer_out);
		rechtUebung1.setUser(breckheimer_out);
		
		//HUENEMOHR
		var huenemohr_out = userRepository.save(huenemohr);
		
		bwlVorlesung.setUser(huenemohr_out);
		bwlUebung.setUser(huenemohr_out);
		bwlUebung1.setUser(huenemohr_out);
		
		//CALVUS
		var calvus_out = userRepository.save(calvus);
		
		auszeichnungsVorlesung.setUser(calvus_out);
		auszeichnungsPraktikum.setUser(calvus_out);
		auszeichnungsPraktikum2.setUser(calvus_out);
		auszeichnungsPraktikum3.setUser(calvus_out);
		auszeichnungsPraktikum4.setUser(calvus_out);
		
		//PEDERSEN
		var pedersen_out = userRepository.save(pedersen);
		
		gemVorlesung.setUser(pedersen_out);
		gemPraktikum.setUser(pedersen_out);
		gemPraktikum2.setUser(pedersen_out);
		gemPraktikum3.setUser(pedersen_out);
		gemPraktikum4.setUser(pedersen_out);
		
		gestaltungVorlesung.setUser(pedersen_out);
		gestaltungPraktikum.setUser(pedersen_out);
		gestaltungPraktikum1.setUser(pedersen_out);
		gestaltungPraktikum2.setUser(pedersen_out);
		gestaltungPraktikum3.setUser(pedersen_out);
		//FRITZ
		var fritz_out = userRepository.save(fritz);
		
		prog3Praktikum3.setUser(fritz_out);
		prog3Praktikum4.setUser(fritz_out);
		
		miVorlesung.setUser(fritz_out);
		emiPraktikum.setUser(fritz_out);
		emiPraktikum2.setUser(fritz_out);
		
		//BERDUX
		var berdux_out = userRepository.save(berdux);
		
		eiboVorlesung.setUser(berdux_out);
		eiboPraktikum.setUser(berdux_out);
		eiboPraktikum2.setUser(berdux_out);
		eiboPraktikum3.setUser(berdux_out);
		eiboPraktikum4.setUser(berdux_out);
		
		prog1Vorlesung.setUser(berdux_out);
		prog1Praktikum.setUser(berdux_out);
		prog1Praktikum2.setUser(berdux_out);
		prog1Praktikum3.setUser(berdux_out);
		
		prog2Vorlesung.setUser(berdux_out);
		prog2Praktikum.setUser(berdux_out);
		prog2Praktikum2.setUser(berdux_out);
		prog2Praktikum3.setUser(berdux_out);
		prog2Praktikum4.setUser(berdux_out);
		
		//Schwanecke
		var schwanecke_out = userRepository.save(schwanecke);
		
		adsVorlesung.setUser(schwanecke_out);
		adsPraktikum.setUser(schwanecke_out);
		adsPraktikum2.setUser(schwanecke_out);
		adsPraktikum3.setUser(schwanecke_out);
		adsPraktikum4.setUser(schwanecke_out);
		
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
		
		TradeOffer offer3 = new TradeOffer();
		offer3.setInstantTrade(true);
		offer3.setOffer(adsPraktikum);

		
		
		List<Module> completedModulesDennis = new ArrayList<>();
//		completedModulesDennis.add(eim);
//		completedModulesDennis.add(prog3);
		dennis.setCompletedModules(completedModulesDennis);
		
	/*	List<Module> completedModulesGandalf = new ArrayList<>();
		completedModulesGandalf.add(ads);
		completedModulesGandalf.add(afs);
		completedModulesGandalf.add(prog3);
		completedModulesGandalf.add(dbs);
		gandalf.setCompletedModules(completedModulesGandalf);*/
		
		System.out.println(String.format("DENNIS WITH ID: %d", dennis.getId()));
		userRepository.saveAll(usersToSave); // saving both at the same time to prevent detached entity exception
		
		PO po2017_repo = poRepository.save(po2017);
		List<String> filters = new ArrayList<>();
		filters.add("CollisionFilter");
		filters.add("CapacityFilter");
		AdminSettings adminSettings = new AdminSettings();
		adminSettings.setId(1);
		adminSettings.setActiveFilters(filters);
		adminSettings.setTradesActive(true);
		adminSettings.setDateStartTrades(LocalDateTime.now());
		adminSettings.setDateEndTrades(LocalDateTime.now().plusDays(15));
		var persistedSettings = adminSettingsRepository.save(adminSettings);
		adminSettingsService.setAdminSettings(persistedSettings);
		
		PO po2017Dual_repo = poRepository.save(po2017Dual);
		
		log.info("--> users " + usersToSave);
		
		CustomPythonFilter testFilter = new CustomPythonFilter("testFilter", "print 'Hello World'");
		testFilter.doFilter(null,null);
		log.warn("Done using testfilter");
		
//		poRepository.findByTitleIs(po2017.getTitle());
		ArrayList<User> students_repo = new ArrayList<>();
		for(User student : usersToSave) {
			User student_repo = userRepository.getOne(student.getId());
			student.setPo(po2017);
			students_repo.add(student_repo);
		}
		po2017_repo.setStudents(students_repo);
		userRepository.saveAll(usersToSave);
		poRepository.save(po2017_repo);
		tradeOfferRepository.save(offer3);
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
		poRestriction.getByProgressiveRegulation().setSemesterSpan(ProgressiveRegulationSpan.NONE);
		poRestriction.getBySemester().setIsActive(false);
		
		PORestriction.DualPO dualPO = new PORestriction.DualPO();
		dualPO.setIsActive(isDual);
		if(isDual) dualPO.setFreeDualDayDefault(DayOfWeek.values()[new Random().nextInt(5)]);
		poRestriction.setDualPO(dualPO);
		
		return poRestriction;
	}
	
}
