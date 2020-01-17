package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
@Table(name = "po")
@Slf4j
public class PO implements Model {
	@Id
	@GeneratedValue
	Long id;
	
	@JsonProperty("title")
	@Column(unique = true, nullable = false, name = "po_title", updatable = true)
//	@Schema(nullable = false, name = "po_title", required = true)
	String title;
	
	@Column(nullable = false)
	@JsonProperty("valid_since_year")
	Long validSinceYear;
	
	@Column(name = "date_start", nullable = false)
//	@Schema(nullable = false, name = "date_start", required = true)
	LocalDate dateStart = LocalDate.now();
	
	@Column(name = "date_end", nullable = true)
//	@Schema(nullable = true, name = "date_end", required = false)
	LocalDate dateEnd;
	
	@Column(nullable = false)
	@JsonProperty("major")
	String major;
	
	@Column(nullable = true, name = "po_modules")
	@OneToMany(mappedBy = "po", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	List<Module> modules;
	
	/**
	 * A flag which will tell whether the {@link PO} is for a dual study only.
	 */
	@Column(nullable = true)
	@JsonInclude(JsonInclude.Include.CUSTOM)
	@JsonProperty("is_dual")
	Boolean isDual;
	
	/**
	 * Custom setter. Will set {@link #isDual} to {@code null} if the given arg is not true. When {@link #isDual} is set to
	 * {@code null} it will not be visible in its JSON/toString representation.
	 *
	 * @param isDual a flag which may either be {@code true}  signifying that the given {@link PO} is only applicable to dual
	 *             study students or {@code null} if it's applicable to non dual
	 *             study students.
	 */
	public void setIsDual(Boolean isDual) {
		this.isDual = isDual ? true : null;
		log.info(String.format("SETTING isDual through '%s' to '%s'", isDual, this.isDual));
	}
	
	public void setDateEnd(LocalDate dateEnd) {
		this.dateEnd = dateEnd == null || dateEnd.isBefore(this.dateStart) ? this.dateStart.plusMonths(6) : this.dateEnd;
	}
}
