package de.hsrm.mi.swtp.exchangeplatform.configuration.initiator;

import com.github.javafaker.Faker;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import de.hsrm.mi.swtp.exchangeplatform.model.data.*;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.DayOfWeek;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.Roles;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.TypeOfTimeslots;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.TypeOfUsers;
import de.hsrm.mi.swtp.exchangeplatform.model.settings.AdminSettings;
import de.hsrm.mi.swtp.exchangeplatform.repository.*;
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
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class DBInitiator implements ApplicationRunner {

	UserRepository userRepository;

	RoomRepository roomRepository;

	AdminSettingsService adminSettingsService;

	AdminSettingsRepository adminSettingsRepository;

	ModuleRepository moduleRepository;

	PORepository poRepository;

	PORestrictionRepository poRestrictionRepository;

	private final Faker faker = Faker.instance();


	private PO initPO(boolean isDual) {
		PO po = new PO();
		po.setTitle(faker.starTrek().specie());
		po.setDateStart(LocalDate.now().plusMonths(faker.random().nextInt(1, 14)));
		po.setMajor(faker.educator().course());
//		Long.valueOf(faker.random().nextInt(2020, 2031))
		Date date = faker.date().future(faker.random().nextInt(365*4, 365*10), TimeUnit.DAYS);
		LocalDate localDate = LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
		po.setValidSince(localDate);

		log.info(" + CREATED DAMN PO: " + po.getMajor());
		return po;
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

	private Module createModule() {
		Module module = new Module();
		module.setName(faker.pokemon().name() + " Studies");
		return module;
	}

	private Timeslot createTimeslot(final TimeTable timeTable, final Module module, final Room room) {
		Timeslot timeslot = new Timeslot();
		timeslot.setTimeSlotType(TypeOfTimeslots.VORLESUNG);
		timeslot.setCapacity(new Random().nextInt(100));
		timeslot.setModule(module);
		DayOfWeek dayOfWeekRndm = DayOfWeek.values()[new Random().nextInt(DayOfWeek.values().length)];
		timeslot.setDay(dayOfWeekRndm);
		timeslot.setTimeStart(LocalTime.of(8, 15));
		timeslot.setTimeEnd(LocalTime.of(9, 45));
		timeslot.setRoom(room);
		timeslot.setTimeTable(timeTable);

		return timeslot;
	}

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

		PORestriction restriction2017 = createRestriction(false);
		restriction2017.getByCP().setIsActive(true);
		restriction2017.getByCP().setMaxCP(40L);
		restriction2017.getByProgressiveRegulation().setIsActive(true);
		restriction2017.getDualPO().setIsActive(false);

		PO po2017 = new PO();
//		restriction2017.setPo(po2017);

		po2017.setTitle(faker.starTrek().specie());
		po2017.setMajor("Medieninformatik");
		po2017.setValidSince(LocalDate.now());
		po2017.setDateStart(LocalDate.now());
		po2017.setDateEnd(LocalDate.now());
		po2017.setRestriction(restriction2017);

		poRepository.save(po2017);

		// END PO 2017

		// START Modul AFS

		PORestriction restrictionNonDual = createRestriction(false);

		PO nonDual = initPO(false);
		nonDual.setRestriction(restrictionNonDual);
		nonDual.setDateStart(LocalDate.now());
		nonDual.setDateEnd(LocalDate.now());
//		restriction2017.setPo(nonDual);

		Module afs = new Module();
		afs.setName("Automaten und formale Sprachen");
		afs.setPo(nonDual);

		// END Modul AFS

		// START Modul Programmieren 3

		PORestriction restrictionDual = createRestriction(true);

		PO dual = initPO(true);
		dual.setRestriction(restrictionDual);
		dual.setDateStart(LocalDate.now());
		dual.setDateEnd(LocalDate.now());
//		restrictionDual.setPo(dual);

		Module prog3 = new Module();
		prog3.setName("Programmieren 3");
		prog3.setPo(dual);

		// END Modul Programmieren 3

//		// START DYNAMIC POs
//
//		ArrayList<Module> modules = new ArrayList<>();
//		modules.add(createModule());
//		PO nonDual = initPO(false, modules);
//
//		ArrayList<Module> modulesDual = new ArrayList<>();
//		modulesDual.add(createModule());
//		PO dual = initPO(true, modulesDual);
//
//		// END DYNAMIC POs

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

		PO po2017Repo = poRepository.findByTitleIs(po2017.getTitle());

		ArrayList<User> po2017Students = new ArrayList<>();
		User dennisRepo = userRepository.findByStudentNumber(dennis.getStudentNumber());
		dennisRepo.setPo(po2017Repo);
		User williRepo = userRepository.findByStudentNumber(willi.getStudentNumber());
		williRepo.setPo(po2017Repo);

		po2017Students.add(dennisRepo);
		po2017Students.add(williRepo);
		po2017Repo.setStudents(po2017Students);

		poRepository.save(po2017Repo);
		poRepository.save(po2017Repo);
		poRepository.save(po2017Repo);

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
