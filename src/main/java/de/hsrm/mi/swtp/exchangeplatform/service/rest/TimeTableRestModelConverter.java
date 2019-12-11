package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.model.data.TimeTable;
import de.hsrm.mi.swtp.exchangeplatform.model.rest_models.TimeslotDTO;
import de.hsrm.mi.swtp.exchangeplatform.model.rest_models.TimetableDTO;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TimeTableRestModelConverter implements RestModelConverter<TimetableDTO> {
	
	TimeslotRestModelConverter timeslotRestModelConverter;
	
	@Autowired
	public TimeTableRestModelConverter(@NotNull TimeslotRestModelConverter timeslotRestModelConverter){
		this.timeslotRestModelConverter = timeslotRestModelConverter;
	}
	
	@Override
	public boolean isResponsible(Object object) {
		return object instanceof TimeTable;
	}
	
	@Override
	public TimetableDTO convertToRest(Object object) {
		TimeTable timetable = (TimeTable) object;
		TimetableDTO out = new TimetableDTO();
		out.setEnd(null);
		out.setStart(null);
		List<TimeslotDTO> timeslotList = new ArrayList<>();
		for(Timeslot timeslot : timetable.getTimeslots()) {
			timeslotList.add(timeslotRestModelConverter.convertToRest(timeslot));
		}
		out.setTimeslots(timeslotList);
		return out;
	}
}
