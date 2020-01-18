package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "po")
@Schema(name = "PO", description = "A PO is a PO is a PO is a PO.")
@RequiredArgsConstructor
@Slf4j
public class PO implements Model {
	@Id
	@GeneratedValue
	@Schema(accessMode = Schema.AccessMode.READ_ONLY)
	Long id;
	
	@Column(name = "po_title", nullable = false, unique = true)
	@Schema(name = "po_title",
			nullable = false,
			required = true,
			description = "A unique human friendly title of a 'PO'.")
	@JsonProperty(value = "title")
	String title;
	
	@Column(name = "valid_since", nullable = false)
	@Schema(name = "valid_since", nullable = false)
	@JsonProperty("valid_since")
	LocalDate validSince = LocalDate.now();
	
	@Column(nullable = false)
	@Schema(name = "semester_count",
			nullable = false,
			format = "int64",
			defaultValue = "6",
			minimum = "1")
	@JsonProperty(value = "semester_count", defaultValue = "6")
	Long semesterCount = 6L;
	
	@Column(name = "date_start", nullable = false)
	@Schema(name = "date_start", nullable = false, required = true)
	@JsonProperty("date_start")
	LocalDate dateStart = LocalDate.now();
	
	@Column(name = "date_end", nullable = true)
//	@Schema(nullable = true, name = "date_end", required = false)
	@JsonProperty("date_end")
	LocalDate dateEnd;
	
	@Column(nullable = false)
	@JsonProperty("major")
	String major;
	
	@Column(nullable = true, name = "po_modules")
	@OneToMany(mappedBy = "po", cascade = CascadeType.ALL, orphanRemoval = true)
	@Schema(name = "po_modules",
			nullable = false,
			defaultValue = "[]",
//			format = "int64",
			description = "A list containing all ids of 'Modules' which are part of the specific 'PO'." )
	@JsonManagedReference
	List<Module> modules;
	
	@OneToOne(cascade = CascadeType.ALL)
	@Schema(required = true, nullable = false, name = "restriction")
	@JsonProperty(value = "restriction", required = true)
	PORestriction poRestriction;
	
	public void setDateEnd(LocalDate dateEnd) {
		this.dateEnd = dateEnd == null || dateEnd.isBefore(this.dateStart) ? this.dateStart.plusMonths(6) : this.dateEnd;
	}
	
}
