package de.hsrm.mi.swtp.exchangeplatform.model.data;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@ToString(exclude = { "timeslots"})
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TimeTable implements Model {
	
	@Id
	@GeneratedValue
	Long id;
	
	@Column(name = "date_start")
	LocalDate dateStart;
	
	@Column(name = "date_end")
	LocalDate dateEnd;
	
	@OneToMany(mappedBy = "timeTable", cascade = CascadeType.ALL, orphanRemoval = true)
	List<Timeslot> timeslots = new ArrayList<>();
	
}
