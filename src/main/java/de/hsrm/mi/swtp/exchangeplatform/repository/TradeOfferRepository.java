package de.hsrm.mi.swtp.exchangeplatform.repository;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeOfferRepository extends JpaRepository<TradeOffer, Long> {
	List<TradeOffer> findAllBySeek(Timeslot seek);
	List<TradeOffer> findAllByOffererAndOffer(User user, Timeslot offer);
	List<TradeOffer> findAllByOfferer(User user);
	TradeOffer findByOffererAndSeek(User user, Timeslot seek);
	List<TradeOffer> findAllByOfferAndSeek(Timeslot offer, Timeslot seek);
	
	List<TradeOffer> findAllByInstantTrade(boolean instantTrade);
}
