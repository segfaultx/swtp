package de.hsrm.mi.swtp.exchangeplatform.model.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hsrm.mi.swtp.exchangeplatform.model.admin.po.enums.ProgressiveRegulationSpan;
import de.hsrm.mi.swtp.exchangeplatform.model.data.PORestriction;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.DayOfWeek;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
	String DUAL_FREE_DAYS;
	
	@Value("${exchangeplatform.default.po.restriction.cp.active}")
	Boolean DEFAULT_CP_ACTIVE;
	@Value("${exchangeplatform.default.po.restriction.cp.max-cp}")
	Long DEFAULT_CP_MAX;
	
	@Value("${exchangeplatform.default.po.restriction.progressive-reg.active}")
	Boolean DEFAULT_PROGRESSIVE_ACTIVE;
	@Value("${exchangeplatform.default.po.restriction.progressive-reg.semester-span}")
	ProgressiveRegulationSpan DEFAULT_PROGRESSIVE_SEMESTER_SPAN;
	
	@Value("${exchangeplatform.default.po.restriction.semester.active}")
	Boolean DEFAULT_SEMESTER_ACTIVE;
	@Value("${exchangeplatform.default.po.restriction.semester.min-semester}")
	Long DEFAULT_SEMESTER_MIN;
	
	ObjectMapper objectMapper;
	
	/** @see PORestrictionFactory */
	public PORestriction createRestriction() {
		PORestriction restriction = new PORestriction();
		restriction.setDualPO(restrictionDual(DEFAULT_DUAL_ACTIVE, DayOfWeek.valueOf(DUAL_FREE_DAY)));
		restriction.setByCP(restrictionCP(DEFAULT_CP_ACTIVE, DEFAULT_CP_MAX));
		restriction.setBySemester(restrictionSemester(DEFAULT_SEMESTER_ACTIVE, DEFAULT_SEMESTER_MIN));
		restriction.setByProgressiveRegulation(restrictionProgressiveRegulation(DEFAULT_PROGRESSIVE_ACTIVE, DEFAULT_PROGRESSIVE_SEMESTER_SPAN));
		return restriction;
	}
	
	private PORestriction.DualPO restrictionDual(boolean isActive, DayOfWeek freeDay) {
		PORestriction.DualPO dualPO = new PORestriction.DualPO();
		dualPO.setIsActive(isActive);
		dualPO.setFreeDualDayDefault(freeDay);
		List<DayOfWeek> days = Arrays.stream(DUAL_FREE_DAYS.split(",")).map(DayOfWeek::valueOf).collect(Collectors.toList());
		dualPO.setFreeDualDays(days);
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
	
	private PORestriction.PORestrictionByProgressiveRegulation restrictionProgressiveRegulation(boolean isActive, ProgressiveRegulationSpan regulationSpan) {
		PORestriction.PORestrictionByProgressiveRegulation byProgressiveRegulation = new PORestriction.PORestrictionByProgressiveRegulation();
		byProgressiveRegulation.setIsActive(isActive);
		byProgressiveRegulation.setSemesterSpan(regulationSpan);
		return byProgressiveRegulation;
	}
	
}
