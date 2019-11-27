package de.hsrm.mi.swtp.exchangeplatform.model.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class TradeOffer implements Model {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Student offerer;

    @ManyToOne
    private Timeslot offer;

    @ManyToOne
    private Timeslot seek;

    private boolean instantTrade = false;

    private boolean accepted = false;
}
