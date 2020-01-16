package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
@Table(name = "po")
@Slf4j
public class PO implements Model {
	@Id
	@GeneratedValue
	private Long id;
	
	@JsonProperty("valid_since_year")
	private String validSinceYear;
	
	@JsonProperty("major")
	private String major;
	
	@OneToMany(mappedBy = "po", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<Module> modules;
	
	/**
	 * A flag which will tell whether the {@link PO} is for a dual study only.
	 */
	@JsonInclude(JsonInclude.Include.CUSTOM)
	@JsonProperty("is_dual")
	private Boolean isDual;
	
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
}
