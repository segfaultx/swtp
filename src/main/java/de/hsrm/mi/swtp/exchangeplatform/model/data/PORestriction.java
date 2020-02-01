package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.hsrm.mi.swtp.exchangeplatform.model.admin.po.enums.ProgressiveRegulationSpan;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.DayOfWeek;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.TypeOfTimeslots;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;

import static java.util.stream.Collectors.groupingBy;

@Entity
@Data
@RequiredArgsConstructor
@Transactional
public class PORestriction implements Model {

	@Id
	@GeneratedValue
	Long id;

	@JsonProperty("by_credit_points")
	@OneToOne(cascade = CascadeType.ALL)
	PORestrictionByCP byCP;

	@JsonProperty("by_semester")
	@OneToOne(cascade = CascadeType.ALL)
	PORestrictionBySemester bySemester;

	@JsonProperty("by_progressive_regulation")
	@OneToOne(cascade = CascadeType.ALL)
	PORestrictionByProgressiveRegulation byProgressiveRegulation;

	@JsonProperty("dual_po")
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	DualPO dualPO;

	@Entity
	@Data
	@ToString(exclude = { "id" })
	@RequiredArgsConstructor
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class PORestrictionByCP implements isPORestriction, PORestrictionType {
		@Id
		@GeneratedValue
		@Schema(hidden = true)
		Long id;

		@JsonProperty("is_active")
		@Schema(name = "is_active", defaultValue = "false", required = true, nullable = false)
		@Column(name = "is_active", nullable = false, updatable = true)
		Boolean isActive = false;

		@Schema(name = "max_credit_points", defaultValue = "0", nullable = true, description = "The maximum amount of CPs a student may cover.")
		@JsonProperty(value = "max_credit_points", defaultValue = "0")
		Long maxCP = 0L;
		
		@Override
		public boolean canAllocate(User user, Module module) {
			return (user.getCompletedModules()
					   .stream()
					   .map(Module::getCreditPoints)
					   .reduce(0L, Long::sum)+module.getCreditPoints()) <= maxCP;
		}
		
		@Override
		public boolean isActive() {
			return this.isActive && maxCP > 0;
		}
	}

	@Entity
	@Data
	@ToString(exclude = { "id" })
	@RequiredArgsConstructor
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class PORestrictionBySemester implements isPORestriction, PORestrictionType {
		@Id
		@GeneratedValue
		@Schema(hidden = true)
		Long id;

		@JsonProperty("is_active")
		@Schema(name = "is_active", defaultValue = "false", required = true, nullable = false)
		@Column(name = "is_active", nullable = false, updatable = true)
		Boolean isActive = false;

		@Schema(name = "min_semesters", defaultValue = "0", nullable = true, description = "All modules have to be passed from the first semester up until this given (inclusive).")
		@Column(nullable = false, name = "min_semesters")
		@JsonProperty(value = "min_semesters", defaultValue = "0")
		Long minSemesters = 0L;
		
		@Override
		public boolean canAllocate(User user, Module module) {
			var po = user.getPo();
			// get all modules of PO per semester
			var modulesPerSemesterMap = po.getModules().stream().collect(groupingBy(Module::getSemester));
			// get all completed modules of student per semester
			var modsPerSemesterStudMap = user.getCompletedModules().stream().collect(groupingBy(Module::getSemester));
			for(long i = 1L; i < minSemesters; i++) {
				// if student didnt complete all modules up to given semester -> return false
				if (!modulesPerSemesterMap.get(i).equals(modsPerSemesterStudMap.get(i))) return false;
			}
			return true;
		}
		
		@Override
		public boolean isActive() {
			return this.isActive && minSemesters > 0;
		}
	}

	@Entity
	@Data
	@ToString(exclude = { "id" })
	@RequiredArgsConstructor
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class PORestrictionByProgressiveRegulation implements isPORestriction, PORestrictionType {
		@Id
		@GeneratedValue
		@Schema(hidden = true)
		Long id;

		@JsonProperty("is_active")
		@Schema(name = "is_active", defaultValue = "false", required = true, nullable = false)
		@Column(name = "is_active", nullable = false, updatable = true)
		Boolean isActive = false;
		
		@Schema(name = "span_between_semesters", nullable = true, defaultValue = "NONE")
		@JsonProperty(value = "span_between_semesters", defaultValue = "NONE")
		ProgressiveRegulationSpan semesterSpan;
		
		@Override
		public boolean canAllocate(User user, Module module) {
			var po = user.getPo();
			// get all modules of PO per semester
			var modulesPerSemesterMap = po.getModules().stream().collect(groupingBy(Module::getSemester));
			// get all completed modules of student per semester
			var modsPerSemesterStudMap = user.getCompletedModules().stream().collect(groupingBy(Module::getSemester));
			long i = 0L;
			for(i = 1L; i < modsPerSemesterStudMap.size(); i++) {
				// if student didnt complete all modules up to given semester -> break, thats the current semester count
				// the student is eligable to allocate semesters
				if (!modulesPerSemesterMap.get(i).equals(modsPerSemesterStudMap.get(i))) break;
			}
			var diff = Math.abs(module.getSemester() - user.getCurrentSemester());
			// if module is within semester span -> student can allocate, else not
			return diff <= semesterSpan.getSemesterSpan();
		}
		
		@Override
		public boolean isActive() {
			return this.isActive && !this.semesterSpan.equals(ProgressiveRegulationSpan.NONE);
		}
	}

	@Entity
	@Data
	@ToString(exclude = { "id" })
	@RequiredArgsConstructor
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class DualPO implements Comparable<DualPO>, isPORestriction, PORestrictionType {
		@Id
		@GeneratedValue
		@Schema(hidden = true)
		Long id;

		/**
		 * A flag which will tell whether the {@link PO} is for a dual study only.
		 */
		@JsonProperty("is_active")
		@Schema(name = "is_active", defaultValue = "false", required = true, nullable = false)
		Boolean isActive = false;
		
		@Schema(name = "free_dual_day_default", nullable = true, required = true, defaultValue = "TUESDAY")
		@JsonProperty(value = "free_dual_day_default", defaultValue = "TUESDAY", required = true)
		DayOfWeek freeDualDayDefault = DayOfWeek.TUESDAY;
		
		@ElementCollection(targetClass = DayOfWeek.class)
		@ArraySchema(schema = @Schema(name = "free_dual_days", nullable = true, required = false, defaultValue = "[]"))
		@JsonProperty(value = "free_dual_days", defaultValue = "[]")
		List<DayOfWeek> freeDualDays;
		
		/**
		 * A getter which will either return the default free day or a {@link DayOfWeek} by semester.
		 */
		public DayOfWeek getFreeDayBySemester(Long semester) {
			if(this.freeDualDays == null || this.freeDualDays.size() <= semester) return this.freeDualDayDefault;
			DayOfWeek freeDayOfSemester = this.freeDualDays.get(Math.toIntExact(semester - 1));
			return freeDayOfSemester == null ? this.freeDualDayDefault : freeDayOfSemester;
		}
		
		public void setFreeDualDayDefault(DayOfWeek freeDualDayDefault) {
			this.freeDualDayDefault = freeDualDayDefault == null ? DayOfWeek.WEDNESDAY : freeDualDayDefault;
		}
		
		@Override
		public int compareTo(DualPO dualPO) {
			String str1 = String.format("%s-%s-%s", this.getId(), this.getIsActive(), this.getFreeDualDayDefault());
			String str2 = String.format("%s-%s-%s", dualPO.getId(), dualPO.getIsActive(), dualPO.getFreeDualDayDefault());
			return str1.compareToIgnoreCase(str2);
		}
		
		@Override
		public boolean canAllocate(User user, Module module) {
			// if students current semester is bigger than regular semestercount, everything goes.
			if (user.getCurrentSemester() > user.getPo().getSemesterCount()) return true;
			// check if VORLESUNG of module is at freeDay, if not user can allocate
			return getFreeDayBySemester(user.getCurrentSemester()) != module.getTimeslots()
						 .stream()
						 .filter(item -> item.getTimeSlotType() == TypeOfTimeslots.VORLESUNG)
					.findFirst()
						 .orElseThrow()
					.getDay();
		}
		
		@Override
		public boolean isActive() {
			return this.isActive;
		}
	}
	
	private PORestrictionType getActiveRestriction(){
		if (this.byCP.isActive) return byCP;
		if (this.byProgressiveRegulation.isActive) return byProgressiveRegulation;
		if (this.bySemester.isActive) return bySemester;
		if (this.dualPO.isActive) return dualPO;
		throw new RuntimeException("No active PO - shouldn't be the case");
	}
	
	public boolean canAllocateModule(User user, Module module){
		return getActiveRestriction().canAllocate(user, module);
	}

}
