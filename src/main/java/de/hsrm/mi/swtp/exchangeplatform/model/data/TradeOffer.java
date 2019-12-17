package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TradeOffer implements Model {
	
	@Id
	@GeneratedValue
	Long id;
	
	@JsonProperty("offerer")
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	User offerer;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	Timeslot offer;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	Timeslot seek;
	
	boolean instantTrade = false;
	
	boolean accepted = false;
}
