package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
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
	
	@JsonSerialize(using = LocalTimeSerializer.class)
	LocalTime timeStart;
	
	@JsonSerialize(using = LocalTimeSerializer.class)
	LocalTime timeEnd;
	
	TypeOfTimeslots timeSlotType;
	
	Integer capacity;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "module_id")
	Module module;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "timetable_id")
	TimeTable timeTable;
}
