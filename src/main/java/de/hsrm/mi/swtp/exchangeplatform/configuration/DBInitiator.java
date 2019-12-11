package de.hsrm.mi.swtp.exchangeplatform.configuration;

import de.hsrm.mi.swtp.exchangeplatform.model.data.*;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import de.hsrm.mi.swtp.exchangeplatform.model.settings.AdminSettings;
import de.hsrm.mi.swtp.exchangeplatform.repository.*;
import de.hsrm.mi.swtp.exchangeplatform.service.settings.AdminSettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Component
public class DBInitiator implements ApplicationRunner {
	
	private final Logger log = LoggerFactory.getLogger(DBInitiator.class);
	
	private final TimeTableRepository timeTableRepository;
	
	private final TradeOfferRepository tradeOfferRepository;
	
	private final AdminSettingsRepository adminSettingsRepository;
	
	private final AdminSettingsService adminSettingsService;
	
	public DBInitiator(TimeTableRepository timeTableRepository, TradeOfferRepository tradeOfferRepository, AdminSettingsRepository adminSettingsRepository,
					   AdminSettingsService adminSettingsService
					  ) {
		this.timeTableRepository = timeTableRepository;
		this.tradeOfferRepository = tradeOfferRepository;
		this.adminSettingsRepository = adminSettingsRepository;
		this.adminSettingsService = adminSettingsService;
	}
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		
		log.info("Filling Database with dark magic");
		
		Lecturer lecturer = new Lecturer();
		lecturer.setFirstName("Wolfgang");
		lecturer.setLastName("Weitz");
		lecturer.setEmail("wolfgang.weitz@hs-rm.de");
		lecturer.setUsername("wweit001");
		
		Student dennis = new Student();
		dennis.setFirstName("Dennis");
		dennis.setLastName("Schad");
		dennis.setStudentId(1006351L);
		dennis.setUsername("dscha001");
		
		Student willi = new Student();
		willi.setFirstName("Willi");
		willi.setLastName("Wusel");
		willi.setStudentId(1005993L);
		willi.setUsername("wwuse001");
		
		Timeslot timeslot2 = new Timeslot();
		timeslot2.setCapacity(20);
		timeslot2.setDay("Montag");
		timeslot2.setTimeStart(LocalTime.of(8, 15));
		timeslot2.setTimeEnd(LocalTime.of(9, 45));
		timeslot2.setType("PRAKTIKUM");
		
		Timeslot timeslot1 = new Timeslot();
		timeslot1.setCapacity(20);
		timeslot1.setDay("Mittwoch");
		timeslot1.setTimeStart(LocalTime.of(8, 15));
		timeslot1.setTimeEnd(LocalTime.of(9, 45));
		
		
		Room d12 = new Room();
		d12.setLocation("Unter den Eichen");
		d12.setRoomNumber("D12");
		timeslot1.setType("PRAKTIKUM");
		
		
		List<Student> attendees = new ArrayList<>();
		attendees.add(dennis);
		
		List<Student> attendees2 = new ArrayList<>();
		attendees2.add(willi);
		
		List<Timeslot> timeslots = new ArrayList<>();
		timeslots.add(timeslot1);
		timeslots.add(timeslot2);
		
		List<Timeslot> dennisTimeslots = new ArrayList<>();
		dennisTimeslots.add(timeslot1);
		
		List<Timeslot> williTimeslots = new ArrayList<>();
		williTimeslots.add(timeslot2);
		
		Module module = new Module();
		module.setName("Softwaretechnik");
		List<Module> modules = new ArrayList<>();
		modules.add(module);
		
		lecturer.setTimeslots(timeslots);
		d12.setTimeslots(timeslots);
		
		timeslot1.setRoom(d12);
		timeslot2.setRoom(d12);
		
		PO po = new PO();
		po.setMajor("Medieninformatik");
		po.setValidSinceYear("2017");
		po.setModules(modules);
		
		module.setPo(po);
		module.setTimeslots(timeslots);
		
		TimeTable timeTable = new TimeTable();
		timeTable.setDateStart(LocalDate.of(2019, 10, 15));
		timeTable.setDateEnd(LocalDate.of(2020, 3, 31));
		timeTable.setTimeslots(timeslots);
		
		timeslot1.setTimeTable(timeTable);
		timeslot1.setModule(module);
		timeslot1.setAttendees(attendees);
		timeslot1.setLecturer(lecturer);
		
		timeslot2.setTimeTable(timeTable);
		timeslot2.setModule(module);
		timeslot2.setAttendees(attendees2);
		timeslot2.setLecturer(lecturer);
		
		
		dennis.setTimeslots(dennisTimeslots);
		willi.setTimeslots(williTimeslots);
		
		TradeOffer tradeOffer = new TradeOffer();
		tradeOffer.setOfferer(willi);
		tradeOffer.setOffer(timeslot2);
		tradeOffer.setSeek(timeslot1);
		
		AdminSettings adminSettings = new AdminSettings();
		List<String> filters = new ArrayList<>();
		filters.add("COLLISION");
		filters.add("CAPACITY");
		adminSettings.updateAdminSettings(true, filters);
		
		tradeOfferRepository.save(tradeOffer);
		timeTableRepository.save(timeTable);
		var persistedSettings = adminSettingsRepository.save(adminSettings);
		adminSettingsService.setAdminSettings(persistedSettings);
		log.info("Done saving timeTable...");
		
	}
}
