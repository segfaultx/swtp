package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.DayOfWeek;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

import static java.util.stream.Collectors.groupingBy;

@Entity
@Data
@RequiredArgsConstructor
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
	@OneToOne(cascade = CascadeType.ALL)
	DualPO dualPO;

	@Entity
	@Data
	@ToString(exclude = { "id" })
	@RequiredArgsConstructor
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class PORestrictionByCP implements PORestrictionType {
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
	}

	@Entity
	@Data
	@ToString(exclude = { "id" })
	@RequiredArgsConstructor
	public static class PORestrictionBySemester implements PORestrictionType {
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
	}

	@Entity
	@Data
	@ToString(exclude = { "id" })
	@RequiredArgsConstructor
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class PORestrictionByProgressiveRegulation implements PORestrictionType {
		@Id
		@GeneratedValue
		@Schema(hidden = true)
		Long id;

		@JsonProperty("is_active")
		@Schema(name = "is_active", defaultValue = "false", required = true, nullable = false)
		@Column(name = "is_active", nullable = false, updatable = true)
		Boolean isActive = false;
		
		@Override
		public boolean canAllocate(User user, Module module) {
			//TODO: ask whats the difference to restriction by semester + implement method
			return false;
		}
	}

	@Entity
	@Data
	@ToString(exclude = { "id" })
	@RequiredArgsConstructor
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class DualPO implements Comparable<DualPO>, PORestrictionType {
		@Id
		@GeneratedValue
		@Schema(hidden = true)
		Long id;

		/**
		 * A flag which will tell whether the {@link PO} is for a dual study only.
		 */
		@JsonProperty("is_active")
		@Schema(name = "is_active", defaultValue = "false", required = true, nullable = false)
		@Column(name = "is_active", nullable = false, updatable = true)
		Boolean isActive = false;

		@Column(nullable = true, name = "free_dual_day")
		@Schema(name = "free_dual_day", nullable = true, required = false, defaultValue = "TUESDAY")
		@JsonProperty(value = "free_dual_day", defaultValue = "TUESDAY")
		DayOfWeek freeDualDay = DayOfWeek.TUESDAY;

		@Override
		public int compareTo(DualPO dualPO) {
			String str1 = String.format("%s-%s-%s", this.getId(), this.getIsActive(), this.getFreeDualDay());
			String str2 = String.format("%s-%s-%s", dualPO.getId(), dualPO.getIsActive(), dualPO.getFreeDualDay());
			return str1.compareToIgnoreCase(str2);
		}
		
		@Override
		public boolean canAllocate(User user, Module module) {
			//TODO: ask how to actually check dualPO restriction and implement method
			return false;
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
