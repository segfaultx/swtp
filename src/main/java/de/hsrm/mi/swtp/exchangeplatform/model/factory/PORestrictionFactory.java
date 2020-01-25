package de.hsrm.mi.swtp.exchangeplatform.model.factory;

import de.hsrm.mi.swtp.exchangeplatform.model.data.PORestriction;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.DayOfWeek;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * A simple factory for creating a {@link PORestriction} instance.
 */
@Component("poRestrictionFactory")
public class PORestrictionFactory {
	
	@Value("${exchangeplatform.default.po.restriction.dual.active}")
	Boolean DEFAULT_DUAL_ACTIVE;
	@Value("${exchangeplatform.default.po.restriction.dual.default-free-day}")
	String DUAL_FREE_DAY;
	@Value("${exchangeplatform.default.po.restriction.dual.default-free-days}")
	List<String> DUAL_FREE_DAYS;
	
	@Value("${exchangeplatform.default.po.restriction.cp.active}")
	Boolean DEFAULT_CP_ACTIVE;
	@Value("${exchangeplatform.default.po.restriction.cp.max-cp}")
	Long DEFAULT_CP_MAX;
	
	@Value("${exchangeplatform.default.po.restriction.progressive-reg.active}")
	Boolean DEFAULT_PROGRESSIVE_ACTIVE;
	
	@Value("${exchangeplatform.default.po.restriction.semester.active}")
	Boolean DEFAULT_SEMESTER_ACTIVE;
	@Value("${exchangeplatform.default.po.restriction.semester.min-semester}")
	Long DEFAULT_SEMESTER_MIN;
	
	/** @see PORestrictionFactory */
	public PORestriction createPO() {
		PORestriction restriction = new PORestriction();
		restriction.setDualPO(restrictionDual(DEFAULT_DUAL_ACTIVE, DayOfWeek.valueOf(DUAL_FREE_DAY)));
		restriction.setByCP(restrictionCP(DEFAULT_CP_ACTIVE, DEFAULT_CP_MAX));
		restriction.setBySemester(restrictionSemester(DEFAULT_SEMESTER_ACTIVE, DEFAULT_SEMESTER_MIN));
		restriction.setByProgressiveRegulation(restrictionProgressiveRegulation(DEFAULT_PROGRESSIVE_ACTIVE));
		return restriction;
	}
	
	private PORestriction.DualPO restrictionDual(boolean isActive, DayOfWeek freeDay) {
		PORestriction.DualPO dualPO = new PORestriction.DualPO();
		dualPO.setIsActive(isActive);
		dualPO.setFreeDualDay(freeDay);
		return dualPO;
	}
	
	private PORestriction.PORestrictionByCP restrictionCP(boolean isActive, Long maxCP) {
		PORestriction.PORestrictionByCP byCP = new PORestriction.PORestrictionByCP();
		byCP.setIsActive(isActive);
		byCP.setMaxCP(maxCP);
		return byCP;
	}
	
	private PORestriction.PORestrictionBySemester restrictionSemester(boolean isActive, Long minSemester) {
		PORestriction.PORestrictionBySemester bySemester = new PORestriction.PORestrictionBySemester();
		bySemester.setIsActive(isActive);
		bySemester.setMinSemesters(minSemester);
		return bySemester;
	}
	
	private PORestriction.PORestrictionByProgressiveRegulation restrictionProgressiveRegulation(boolean isActive) {
		PORestriction.PORestrictionByProgressiveRegulation byProgressiveRegulation = new PORestriction.PORestrictionByProgressiveRegulation();
		byProgressiveRegulation.setIsActive(isActive);
		return byProgressiveRegulation;
	}
	
}
