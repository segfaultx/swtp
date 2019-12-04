package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.hsrm.mi.swtp.exchangeplatform.model.serializer.POSerializer;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
@JsonSerialize(using = POSerializer.class)
public class PO implements Model {
	@Id
	@GeneratedValue
	private Long id;
	
	@JsonProperty("valid_since_year")
	private String validSinceYear;
	
	private String major;
	
	@JsonIgnore
	@JsonProperty("modules")
	@OneToMany(mappedBy = "po", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Module> modules;
}
