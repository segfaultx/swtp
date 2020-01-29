package de.hsrm.mi.swtp.exchangeplatform.model.factory;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import de.hsrm.mi.swtp.exchangeplatform.model.data.PO;
import de.hsrm.mi.swtp.exchangeplatform.model.data.PORestriction;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple factory for creating a {@link PO} instance.
 */
@Component("poFactory")
public class POFactory {
	
	@Value("${exchangeplatform.default.po.title}")
	String DEFAULT_TITLE;
	
	@Value("${exchangeplatform.default.po.major}")
	String DEFAULT_MAJOR;
	
	@Value("${exchangeplatform.default.po.semesters}")
	Long DEFAULT_SEMESTER_COUNT;
	
	/** @see POFactory */
	public PO createPO(final LocalDate validSince,
					   final LocalDate dateStart,
					   final LocalDate dateEnd
					  ) {
		PO po = new PO();
		po.setTitle(DEFAULT_TITLE);
		po.setMajor(DEFAULT_MAJOR);
		po.setSemesterCount(DEFAULT_SEMESTER_COUNT);
		po.setValidSince(validSince);
		po.setDateStart(dateStart);
		po.setDateEnd(dateEnd);
		return this.createPO(DEFAULT_TITLE,
							 DEFAULT_MAJOR,
							 DEFAULT_SEMESTER_COUNT,
							 validSince,
							 dateStart,
							 dateEnd);
	}
	
	/** @see POFactory */
	public PO createPO(final String title,
					   final String major,
					   final Long semesterCount,
					   final LocalDate validSince,
					   final LocalDate dateStart,
					   final LocalDate dateEnd
					   ) {
		return this.createPO(title,
							 major,
							 semesterCount,
							 validSince,
							 dateStart,
							 dateEnd,
							 new ArrayList<>(),
							 new ArrayList<>(),
							 null);
	}
	
	/** @see POFactory */
	public PO createPO(final String title,
					   final String major,
					   final Long semesterCount,
					   final LocalDate validSince,
					   final LocalDate dateStart,
					   final LocalDate dateEnd,
					   final List<Module> modules,
					   final List<User> students,
					   final PORestriction restriction
					   ) {
		PO po = new PO();
		po.setTitle(title);
		po.setMajor(major);
		po.setSemesterCount(semesterCount);
		po.setValidSince(validSince);
		po.setDateStart(dateStart);
		po.setDateEnd(dateEnd);
		po.setModules(modules);
		po.setStudents(students);
		po.setRestriction(restriction);
		return po;
	}
	
	public PO clone(final PO po) {
		final PO clone = createPO(
				po.getTitle(),
				po.getMajor(),
				po.getSemesterCount(),
				po.getValidSince(),
				po.getDateStart(),
				po.getDateEnd(),
				po.getModules(),
				po.getStudents(),
				po.getRestriction());
		
		return clone;
	}
	
}
