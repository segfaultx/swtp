package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.DayOfWeek;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.TypeOfTimeslots;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Timeslot implements Model {
	
	@Id
	@GeneratedValue
	Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="USER_ID")
	@JsonBackReference
    User user;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	Room room;
	
	@Enumerated(EnumType.STRING)
	DayOfWeek day;
	
	LocalTime timeStart;
	
	LocalTime timeEnd;
	
	TypeOfTimeslots timeSlotType;
	
	Integer capacity;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "MODULE_ID")
	@JsonBackReference
	Module module;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "TIMETABLE_ID")
	@JsonBackReference
	TimeTable timeTable;
}
