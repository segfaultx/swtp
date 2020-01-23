package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@ToString(exclude = { "timeslots" })
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TimeTable implements Model {
	
	@Id
	@GeneratedValue
	Long id;
	
	@Column(name = "date_start")
	@Schema(nullable = true)
	LocalDate dateStart;
	
	@Column(name = "date_end")
	@Schema(nullable = true)
	LocalDate dateEnd;
	
	@OneToMany(mappedBy = "timeTable", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference("timetable-timeslots")
	List<Timeslot> timeslots = new ArrayList<>();
	
}
