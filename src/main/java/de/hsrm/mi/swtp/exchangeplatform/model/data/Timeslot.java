package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.DayOfWeek;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.TypeOfTimeslots;
import de.hsrm.mi.swtp.exchangeplatform.model.serializer.LocalTimeSerializer;
import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@ToString(exclude = { "user", "room", "module", "timeTable", "attendees"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Timeslot implements Model {
	
	@Id
	@GeneratedValue
	Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	@JsonBackReference
    User user;
	
	@ManyToOne
	Room room;
	
	@Enumerated(EnumType.STRING)
	DayOfWeek day;
	
	@JsonSerialize(using = LocalTimeSerializer.class)
	LocalTime timeStart;
	
	@JsonSerialize(using = LocalTimeSerializer.class)
	LocalTime timeEnd;
	
	TypeOfTimeslots timeSlotType;
	
	Integer capacity;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "MODULE_ID")
	Module module;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "TIMETABLE_ID")
	@JsonBackReference
	TimeTable timeTable;
	
	@ManyToMany(mappedBy = "timeslots", fetch = FetchType.LAZY)
	@JsonBackReference
	List<User> attendees = new ArrayList<>();
	
	@ManyToMany(mappedBy = "waitLists", fetch = FetchType.EAGER)
	@JsonBackReference
	List<User> waitList = new ArrayList<>();
	
}
