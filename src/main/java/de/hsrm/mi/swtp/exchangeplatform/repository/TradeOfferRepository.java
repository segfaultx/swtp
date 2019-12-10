package de.hsrm.mi.swtp.exchangeplatform.repository;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeOfferRepository extends JpaRepository<TradeOffer, Long> {
	List<TradeOffer> findAllBySeek(Timeslot seek);
	List<TradeOffer> findAllBySeekAndInstantTrade(Timeslot seek, boolean instantTrade);
}
