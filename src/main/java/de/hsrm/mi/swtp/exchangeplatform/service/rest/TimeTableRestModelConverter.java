package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.model.data.TimeTable;
import de.hsrm.mi.swtp.exchangeplatform.model.rest_models.Timetable;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TimeTableRestModelConverter implements RestModelConverter {
	
	TimeslotRestModelConverter timeslotRestModelConverter;
	
	@Override
	public boolean isResponsible(Object object) {
		return object instanceof TimeTable;
	}
	
	@Override
	public Object convertToRest(Object object) {
		TimeTable timetable = (TimeTable) object;
		Timetable out = new Timetable();
		out.setEnd(timetable.getDateEnd());
		out.setStart(timetable.getDateStart());
		out.setId(timetable.getId());
		List<de.hsrm.mi.swtp.exchangeplatform.model.rest_models.Timeslot> timeslotList = new ArrayList<>();
		for(Timeslot timeslot : timetable.getTimeslots()) {
			timeslotList.add((de.hsrm.mi.swtp.exchangeplatform.model.rest_models.Timeslot) timeslotRestModelConverter.convertToRest(timeslot));
		}
		out.setTimeslots(timeslotList);
		return out;
	}
}
