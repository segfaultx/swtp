package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.hsrm.mi.swtp.exchangeplatform.model.serializer.POSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@ToString(exclude = { "modules" })
@Table(name = "po")
@Schema(name = "PO", description = "A PO is a PO is a PO is a PO.")
@RequiredArgsConstructor
@Slf4j
@JsonSerialize(using = POSerializer.class)
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
	
	@JsonProperty("modules")
	@OneToMany(cascade = CascadeType.ALL)
//	@JoinColumn(name = "po_modules")
	@JsonManagedReference("po-modules")
	@JsonIgnore
	List<Module> modules;
	
	@JsonProperty("students")
	@OneToMany(cascade = CascadeType.ALL)
//	@JoinColumn(name = "po_modules")
	@JsonManagedReference("po-students")
	@JsonIgnore
	List<User> students;
	
	@OneToOne(cascade = CascadeType.ALL)
	@Schema(required = true, nullable = false, name = "restriction")
	@JsonProperty(value = "restriction", required = true)
	@JsonManagedReference("po-restriction")
	PORestriction poRestriction;
	
}
