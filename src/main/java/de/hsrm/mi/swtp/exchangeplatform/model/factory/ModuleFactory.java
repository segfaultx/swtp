package de.hsrm.mi.swtp.exchangeplatform.model.factory;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import de.hsrm.mi.swtp.exchangeplatform.model.data.PO;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple factory for creating a {@link Module} instance.
 */
@Component("moduleFactory")
public class ModuleFactory {
	
	@Value("${exchangeplatform.default.module.semester}")
	Long DEFAULT_SEMESTER;
	
	@Value("${exchangeplatform.default.module.credit-points}")
	Long DEFAULT_CREDIT_POINTS;
	
	boolean DEFAULT_ACTIVE = true;
	
	/** @see ModuleFactory */
	public Module createModule(final String name,
									  final PO po) {
		return createModule(name, null, po, DEFAULT_SEMESTER, DEFAULT_CREDIT_POINTS);
	}
	
	/** @see ModuleFactory */
	public Module createModule(final String name,
									  final PO po,
									  final Long semester) {
		return createModule(name, null, po, semester, DEFAULT_CREDIT_POINTS);
	}
	
	/** @see ModuleFactory */
	public Module createModule(final String name,
									  final User lecturer,
									  final PO po,
									  final Long semester,
									  final Long creditPoints) {
		return createModule(name, lecturer, po, semester, creditPoints, new ArrayList<>(), new ArrayList<>());
	}
	/** @see ModuleFactory */
	public Module createModule(final String name,
							   final User lecturer,
							   final PO po,
							   final Long semester,
							   final Long creditPoints,
							   @NonNull final List<User> attendees,
							   @NonNull final List<Timeslot> timeslots) {

		return createModule(name, lecturer, po, semester, creditPoints, attendees, timeslots, DEFAULT_ACTIVE);
	}
	public Module createModule(final String name,
							   final PO po,
							   final Long semester,
							   final boolean isActive){
		return createModule(name, null, po ,semester, DEFAULT_CREDIT_POINTS, new ArrayList<>(), new ArrayList<>(), isActive);
	}
	
	
	/** @see ModuleFactory */
	public Module createModule(final String name,
									  final User lecturer,
									  final PO po,
									  final Long semester,
									  final Long creditPoints,
									  @NonNull final List<User> attendees,
									  @NonNull final List<Timeslot> timeslots,
							          final boolean isActive) {
		Module module = new Module();
		module.setName(name);
		module.setLecturer(lecturer);
		module.setPo(po);
		module.setSemester(semester);
		module.setCreditPoints(creditPoints);
		module.setAttendees(attendees);
		module.setTimeslots(timeslots);
		module.setIsActive(isActive);
		
		return module;
	}

	
	
}
