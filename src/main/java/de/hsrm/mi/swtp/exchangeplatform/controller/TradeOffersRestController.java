package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.TradeOfferNotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.TradeOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trades")
public class TradeOffersRestController {

    @Autowired
    private TradeOfferService tradeOfferService;


    @GetMapping
    public List<TradeOffer> getTradeOffers(){
        return tradeOfferService.getTrades();
    }

    /**
     * DELETE request handler.
     * provides an endpoint to {@code '/api/v1/trades/<id>/<id>'} through which an {student} may delete his {@link TradeOffer}.
     *
     * @param studentId studentId of requester
     * @param tradeId tradeId of tradeoffer which is to be deleted.
     * @return {@link HttpStatus#OK} if tradeoffer was deleted, {@link HttpStatus#NOT_FOUND} if tradeoffer wasnt found,
     * {@link HttpStatus#BAD_REQUEST} if requester isnt owner of the tradeoffer.
     */
    @DeleteMapping("/{studentId}/{tradeId}")
    public ResponseEntity deleteTradeOffer(@PathVariable("studentId") long studentId, @PathVariable("tradeId") long tradeId){
        try{
            if (tradeOfferService.deleteTradeOffer(studentId, tradeId))return new ResponseEntity(HttpStatus.OK);
        }catch(TradeOfferNotFoundException ex){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    /**
     * POST request handler.
     * provides an endpoint to {@code '/api/v1/trades/<id>/<id>/<id>} through which an student may insert a new {@link TradeOffer}
     *
     *
     * @param studentId id of requester
     * @param offerId id of timeslot which requester is offering
     * @param seekId id of sought timeslot
     * @return {@link HttpStatus#OK} plus the new {@link TradeOffer} instance which has been created,
     * {@link HttpStatus#INTERNAL_SERVER_ERROR} if the server encountered an error.
     */
    @PostMapping("/{studentId}/{offer}/{seek}")
    public ResponseEntity<TradeOffer> createTradeOffer(@PathVariable("studentId") long studentId,
                                       @PathVariable("offer") long offerId,
                                       @PathVariable("seek") long seekId){
        var tradeoffer = tradeOfferService.createTradeOffer(studentId, offerId, seekId);
        if (tradeoffer == null) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(tradeoffer, HttpStatus.OK);
    }
}
