package de.hsrm.mi.swtp.exchangeplatform.model.factory;

import com.github.javafaker.Bool;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import de.hsrm.mi.swtp.exchangeplatform.model.data.*;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.DayOfWeek;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.TypeOfTimeslots;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

/**
 * A simple factory for creating a {@link Timeslot} instance.
 */
@Component("timeslotFactory")
public class TimeslotFactory {
	
	@Value("${exchangeplatform.default.timeslot.capacity.praktikum}")
	Integer DEFAULT_CAPACITY_PRAKTIKUM;
	
	@Value("${exchangeplatform.default.timeslot.capacity.uebung}")
	Integer DEFAULT_CAPACITY_UEBUNG;
	
	@Value("${exchangeplatform.default.timeslot.capacity.vorlesung}")
	Integer DEFAULT_CAPACITY_VORLESUNG;
	
	@Value("${exchangeplatform.default.timeslot.duration}")
	Integer DEFAULT_DURATION;
	
	@Value("${exchangeplatform.default.timeslot.tradeable.other}")
	Boolean DEFAULT_TRADEABLE_OTHERS;
	
	@Value("${exchangeplatform.default.timeslot.tradeable.lecture}")
	Boolean DEFAULT_TRADEABLE_LECTURE;
	
	String DEFAULT_GROUP_NAME = "";
	
	/** @see TimeslotFactory */
	public Timeslot createTimeslotPraktikum(final DayOfWeek dayOfWeek,
											       final String group,
												   final Module module,
												   final LocalTime timeStart,
												   final Room room) {
		return createTimeslotPraktikum(dayOfWeek, group, DEFAULT_TRADEABLE_OTHERS, module, timeStart, timeStart.plusMinutes(DEFAULT_DURATION), room);
	}
	
	/** @see TimeslotFactory */
	public Timeslot createTimeslotPraktikum(final DayOfWeek dayOfWeek,
											       final String group,
											       final Boolean isTradeable,
												   final Module module,
												   final LocalTime timeStart,
												   final LocalTime timeEnd,
												   final Room room) {
		return createTimeslotPraktikum(dayOfWeek, group, isTradeable, DEFAULT_CAPACITY_PRAKTIKUM, null, module, timeStart, timeEnd, room);
	}
	
	/** @see TimeslotFactory */
	public Timeslot createTimeslotPraktikum(final DayOfWeek dayOfWeek,
											       final String group,
											       final Boolean isTradeable,
												   final Integer capacity,
												   final User lecturer,
												   final Module module,
												   final LocalTime timeStart,
												   final LocalTime timeEnd,
												   final Room room) {
		return createTimeslot(TypeOfTimeslots.PRAKTIKUM, dayOfWeek, group, isTradeable, capacity, lecturer, module, timeStart, timeEnd, room, null);
	}
	
	/** @see TimeslotFactory */
	public Timeslot createTimeslotUebung(final DayOfWeek dayOfWeek,
										           final String group,
												   final Module module,
												   final LocalTime timeStart,
												   final Room room) {
		return createTimeslotUebung(dayOfWeek, group, DEFAULT_TRADEABLE_OTHERS, module, timeStart, timeStart.plusMinutes(DEFAULT_DURATION), room);
	}
	
	/** @see TimeslotFactory */
	public Timeslot createTimeslotUebung(final DayOfWeek dayOfWeek,
										           final String group,
										           final Boolean isTradeable,
												   final Module module,
												   final LocalTime timeStart,
												   final LocalTime timeEnd,
												   final Room room) {
		return createTimeslotUebung(dayOfWeek, group, isTradeable, DEFAULT_CAPACITY_UEBUNG, null, module, timeStart, timeEnd, room);
	}
	
	/** @see TimeslotFactory */
	public Timeslot createTimeslotUebung(final DayOfWeek dayOfWeek,
										           final String group,
										           final Boolean isTradeable,
												   final Integer capacity,
												   final User lecturer,
												   final Module module,
												   final LocalTime timeStart,
												   final LocalTime timeEnd,
												   final Room room) {
		return createTimeslot(TypeOfTimeslots.UEBUNG, dayOfWeek, group, isTradeable, capacity, lecturer, module, timeStart, timeEnd, room, null);
	}
	
	/** @see TimeslotFactory */
	public Timeslot createTimeslotVorlesung(final DayOfWeek dayOfWeek,
												   final Module module,
												   final LocalTime timeStart,
												   final Room room) {
		return createTimeslotVorlesung(dayOfWeek, DEFAULT_GROUP_NAME, DEFAULT_TRADEABLE_LECTURE, module, timeStart, timeStart.plusMinutes(DEFAULT_DURATION), room);
	}
	
	/** @see TimeslotFactory */
	public Timeslot createTimeslotVorlesung(final DayOfWeek dayOfWeek,
											       final String group,
											       final Boolean isTradeable,
												   final Module module,
												   final LocalTime timeStart,
												   final LocalTime timeEnd,
												   final Room room) {
		return createTimeslotVorlesung(dayOfWeek, group, isTradeable, DEFAULT_CAPACITY_VORLESUNG, null, module, timeStart, timeEnd, room);
	}
	
	/** @see TimeslotFactory */
	public Timeslot createTimeslotVorlesung(final DayOfWeek dayOfWeek,
											       final String group,
											       final Boolean isTradeable,
												   final Integer capacity,
												   final User lecturer,
												   final Module module,
												   final LocalTime timeStart,
												   final LocalTime timeEnd,
												   final Room room) {
		return createTimeslot(TypeOfTimeslots.VORLESUNG, dayOfWeek, group, isTradeable, capacity, lecturer, module, timeStart, timeEnd, room, null);
	}
	
	/** @see TimeslotFactory */
	public Timeslot createTimeslot(final TypeOfTimeslots type,
										  final DayOfWeek dayOfWeek,
								          final String group,
										  final Boolean isTradeable,
										  final Integer capacity,
										  final User lecturer,
										  final Module module,
										  final LocalTime timeStart,
										  final LocalTime timeEnd,
										  final Room room,
										  final TimeTable timeTable) {
		Timeslot timeslot = new Timeslot();
		timeslot.setTimeSlotType(type);
		timeslot.setDay(dayOfWeek);
		timeslot.setPracticalGroup(group);
		timeslot.setIsTradeable(isTradeable);
		timeslot.setCapacity(capacity);
		timeslot.setUser(lecturer);
		timeslot.setModule(module);
		timeslot.setTimeStart(timeStart);
		timeslot.setTimeEnd(timeEnd);
		timeslot.setRoom(room);
		timeslot.setTimeTable(timeTable);
		
		return timeslot;
	}
	
}
