package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.DayOfWeek;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@ToString(exclude = { "id" })
@RequiredArgsConstructor
public class PORestriction implements Model {
	
	@Id
	@GeneratedValue
	@Schema(hidden = true)
	@JsonIgnore
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
	
	@JsonBackReference
	@OneToOne(cascade = CascadeType.ALL)
	PO po;
	
	@Entity
	@Data
	@ToString(exclude = { "id" })
	@RequiredArgsConstructor
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class PORestrictionByCP {
		@Id
		@GeneratedValue
		@Schema(hidden = true)
		@JsonIgnore
		Long id;
		
		@JsonProperty("is_active")
		@Schema(name = "is_active",
				defaultValue = "false",
				required = true,
				nullable = false)
		@Column(name = "is_active", nullable = false, updatable = true)
		Boolean isActive = false;
		
		@Schema(name = "max_credit_points",
				defaultValue = "0",
				nullable = true,
				description = "The maximum amount of CPs a student may cover.")
		@JsonProperty(value = "max_credit_points", defaultValue = "0")
		Long maxCP = 0L;
	}
	
	@Entity
	@Data
	@ToString(exclude = { "id" })
	@RequiredArgsConstructor
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class PORestrictionBySemester {
		@Id
		@GeneratedValue
		@Schema(hidden = true)
		@JsonIgnore
		Long id;
		
		@JsonProperty("is_active")
		@Schema(name = "is_active",
				defaultValue = "false",
				required = true,
				nullable = false)
		@Column(name = "is_active", nullable = false, updatable = true)
		Boolean isActive = false;
		
		@Schema(name = "min_semesters",
				defaultValue = "0",
				nullable = true,
				description = "All modules have to be passed from the first semester up until this given (inclusive).")
		@Column(nullable = false, name = "min_semesters")
		@JsonProperty(value = "min_semesters", defaultValue = "0")
		Integer minSemesters = 0;
	}
	
	@Entity
	@Data
	@ToString(exclude = { "id" })
	@RequiredArgsConstructor
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class PORestrictionByProgressiveRegulation {
		@Id
		@GeneratedValue
		@Schema(hidden = true)
		@JsonIgnore
		Long id;
		
		@JsonProperty("is_active")
		@Schema(name = "is_active",
				defaultValue = "false",
				required = true,
				nullable = false)
		@Column(name = "is_active", nullable = false, updatable = true)
		Boolean isActive = false;
	}
	
	@Entity
	@Data
	@ToString(exclude = { "id" })
	@RequiredArgsConstructor
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class DualPO {
		@Id
		@GeneratedValue
		@Schema(hidden = true)
		@JsonIgnore
		Long id;
		
		/**
		 * A flag which will tell whether the {@link PO} is for a dual study only.
		 */
		@JsonProperty("is_active")
		@Schema(name = "is_active",
				defaultValue = "false",
				required = true,
				nullable = false)
		@Column(name = "is_active", nullable = false, updatable = true)
		Boolean isActive = false;
		
		@Column(nullable = true, name = "free_dual_day")
		@Schema(name = "free_dual_day",
				nullable = true,
				required = false,
				defaultValue = "TUESDAY")
		@JsonProperty(value = "free_dual_day", defaultValue = "TUESDAY")
		DayOfWeek freeDualDay = DayOfWeek.TUESDAY;
	}
	
}
