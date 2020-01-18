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
	
	private User createStudent(Boolean isStudent, Boolean isAdmin) {
		String fName = faker.name().firstName();
		String lName = faker.name().lastName();
		String uName = String.format("%s%s001", fName.charAt(0), lName.substring(0, lName.length() - 1));
		
		Long idNum = Long.valueOf(faker.random().nextInt(1000000, 9999999));
		User user = new User();
		user.setFirstName(fName);
		user.setLastName(lName);
		user.setStudentNumber(isStudent ? idNum : null);
		user.setStaffNumber(!isStudent ? idNum : null);
		user.setEmail(String.format("%s.%s@hs-rm.de", fName, lName));
		
		AuthenticationInformation userInformation = new AuthenticationInformation();
		userInformation.setUsername(uName);
		userInformation.setPassword(uName);
		userInformation.setRole(isAdmin ? Roles.ADMIN : Roles.MEMBER);
		userInformation.setUser(user);
		user.setAuthenticationInformation(userInformation);
		
		UserType userType = new UserType();
		userType.setType(TypeOfUsers.STUDENT);
		userType.setUser(user);
		user.setUserType(userType);
		
		return user;
	}
	
	private PO initPO() {
		PORestriction.PORestrictionByCP byCP = new PORestriction.PORestrictionByCP();
		byCP.setIsActive(true);
		byCP.setMaxCP(60L);
		
		PORestriction.PORestrictionBySemester bySemester = new PORestriction.PORestrictionBySemester();
		bySemester.setIsActive(true);
		bySemester.setMinSemesters(4);
		
		PORestriction.PORestrictionByProgressiveRegulation byProgressiveRegulation = new PORestriction.PORestrictionByProgressiveRegulation();
		bySemester.setIsActive(true);
		
		PORestriction.DualPO isDualPO = new PORestriction.DualPO();
		isDualPO.setIsActive(true);
		isDualPO.setFreeDualDay(DayOfWeek.THURSDAY);
		
		PORestriction po20Restriction = new PORestriction();
		po20Restriction.setDualPO(isDualPO);
		po20Restriction.setBySemester(bySemester);
		po20Restriction.setByProgressiveRegulation(byProgressiveRegulation);
		po20Restriction.setByCP(byCP);
		
		PO po20 = new PO();
		po20.setDateStart(LocalDate.now());
		po20.setDateEnd(LocalDate.now().plusMonths(6));
		po20.setPoRestriction(po20Restriction);
		po20.setTitle("Medieninformatik 2k20");
		po20.setModules(new ArrayList<>());
		po20.setMajor(faker.educator().course());
		po20.setSemesterCount(7L);
		po20.setValidSince(LocalDate.now());
		
		return po20;
	}
	
	private User createStudent(String fName, String lName, Long studentId) {
		UserType userType = new UserType();
		userType.setType(TypeOfUsers.STUDENT);
		
		String uName = String.format("%s%s001", fName.charAt(0), lName.substring(0, lName.length() - 1));
		
		AuthenticationInformation authenticationInformation = new AuthenticationInformation();
		authenticationInformation.setUsername(uName);
		authenticationInformation.setPassword(uName);
		authenticationInformation.setRole(Roles.MEMBER);
		
		User user = new User();
		user.setFirstName(fName);
		user.setLastName(lName);
		user.setEmail(String.format("%s.%s@hs-rm.de", fName, lName));
		user.setStudentNumber(studentId);
		user.setStaffNumber(null);
		user.setTradeoffers(new ArrayList<>());
		user.setTimeslots(new ArrayList<>());
		user.setCompletedModules(new ArrayList<>());
		user.setUserType(userType);
		user.setAuthenticationInformation(authenticationInformation);
		
		return null;
	}
	
	private User createLecturer(String fName, String lName, Long id, Boolean isAdmin) {
		UserType userType = new UserType();
		userType.setType(TypeOfUsers.LECTURER);
		
		String uName = String.format("%s%s001", fName.charAt(0), lName.substring(0, lName.length() - 1)).toLowerCase();
		
		AuthenticationInformation authenticationInformation = new AuthenticationInformation();
		authenticationInformation.setUsername(uName);
		authenticationInformation.setPassword(uName);
		authenticationInformation.setRole(isAdmin ? Roles.ADMIN : Roles.MEMBER);
		
		User user = new User();
		user.setFirstName(fName);
		user.setLastName(lName);
		user.setEmail(String.format("%s.%s@hs-rm.de", fName, lName));
		user.setStudentNumber(null);
		user.setStaffNumber(id);
		user.setTradeoffers(new ArrayList<>());
		user.setTimeslots(new ArrayList<>());
		user.setCompletedModules(new ArrayList<>());
		
		user.setUserType(userType);
		user.setAuthenticationInformation(authenticationInformation);
		
		return user;
	}
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		
		log.info("Filling Database with dark magic");
		
		/////////////////////////////////////////////////////////////////////
		
		PO po20 = initPO();
		
		User weitz = createLecturer("Wolfgang", "Weitz", 171717L, true);
		
		
		
		
		
		
		
		
		
		
		
		
		/////////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////
		
		userRepository.saveAndFlush(weitz);
		poRepository.saveAndFlush(po20);
		
		/////////////////////////////////////////////////////////////////////
		
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
