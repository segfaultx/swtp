package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;


/**
 * @author amatus
 */
@Entity
@Data
@ToString(exclude = { "offerer", "offer", "seek"})
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TradeOffer implements Model {
	
	@Id
	@GeneratedValue
	Long id;
	
	@JsonProperty("seeker")
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference("seeker-tradeoffers")
	User seeker;
	
	@JsonProperty("offerer")
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference("offerer-tradeoffers")
	User offerer;
	
	@ManyToOne(fetch = FetchType.LAZY)
	Timeslot offer;
	
	@ManyToOne(fetch = FetchType.LAZY)
	Timeslot seek;
	
	boolean instantTrade = false;
	
	boolean accepted = false;
}
