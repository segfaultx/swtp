package de.hsrm.mi.swtp.exchangeplatform.model.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class TradeOffer implements Model {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Student offerer;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Timeslot offer;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Timeslot seek;

    private boolean instantTrade = false;

    private boolean accepted = false;
}
