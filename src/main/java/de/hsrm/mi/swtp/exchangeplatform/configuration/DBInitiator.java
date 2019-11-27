package de.hsrm.mi.swtp.exchangeplatform.configuration;

import de.hsrm.mi.swtp.exchangeplatform.model.data.*;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import de.hsrm.mi.swtp.exchangeplatform.repository.*;
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

    public DBInitiator(TimeTableRepository timeTableRepository) {
        this.timeTableRepository = timeTableRepository;
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
        dennis.setMatriculationNumber(1006351L);
        dennis.setUsername("dscha001");

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

        List<Timeslot> timeslots = new ArrayList<>();
        timeslots.add(timeslot1);

        Module module = new Module();
        module.setName("Softwaretechnik");

        lecturer.setTimeslots(timeslots);
        d12.setTimeslots(timeslots);

        timeslot1.setRoom(d12);

        PO po = new PO();
        po.setMajor("Medieninformatik");
        po.setValidSinceYear("2017");

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


        dennis.setTimeslots(timeslots);

        timeTableRepository.save(timeTable);

        log.info("Done saving timeTable...");

    }
}
