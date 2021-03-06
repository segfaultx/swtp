package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.DayOfWeek;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.TypeOfTimeslots;
import de.hsrm.mi.swtp.exchangeplatform.model.serializer.TimeslotSerializer;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
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
@ToString(exclude = { "user", "room", "module", "timeTable", "attendees", "waitList" })
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonSerialize(using = TimeslotSerializer.class)
public class Timeslot implements Model {
	
	@Id
	@GeneratedValue
	Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	User user;
	
	@ManyToOne
	Room room;
	
	@Enumerated(EnumType.STRING)
	DayOfWeek day;
	
	@Schema(type = "string", format = "partial-time")
	LocalTime timeStart;
	
	@Schema(type = "string", format = "partial-time")
	LocalTime timeEnd;
	
	TypeOfTimeslots timeSlotType;
	
	String practicalGroup;
	
	Integer capacity;
	
	Boolean	isTradeable;
	
	String userInitials;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "MODULE_ID")
	@Schema(type = "object")
	@JsonBackReference("module-timeslots")
	Module module;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "TIMETABLE_ID")
	@Schema(nullable = true)
	@JsonBackReference("timetable-timeslots")
	TimeTable timeTable;
	
	@ManyToMany(mappedBy = "timeslots", fetch = FetchType.LAZY)
	@ArraySchema(schema = @Schema(type = "integer", format = "int64"))
	@JsonBackReference("user-timeslots")
	List<User> attendees = new ArrayList<>();
	
	@ManyToMany(mappedBy = "waitLists", fetch = FetchType.LAZY)
	@JsonBackReference("student-waitlist")
	List<User> waitList = new ArrayList<>();
	
	/**
	 * Method to add a user to this timeslot
	 * @param user user to add
	 */
	public void addAttendee(User user) {
		attendees.add(user);
		user.getTimeslots().add(this);
	}
	
	/**
	 * Method to remove a user from this timeslot
	 * @param user user to remove
	 */
	public void removeAttendee(User user) {
		attendees.remove(user);
		user.getTimeslots().remove(this);
	}
	
	@Override
	public boolean equals(Object other){
		if (!(other instanceof Timeslot)) return false;
		Timeslot other_casted = (Timeslot) other;
		return this.id.equals(((Timeslot) other).getId());
	}
	
}
