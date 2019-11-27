package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.TradeOfferNotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import de.hsrm.mi.swtp.exchangeplatform.model.rest_models.TradeRequest;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.TradeOfferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/trades")
@Slf4j
public class TradeOffersRestController {

    @Autowired
    private TradeOfferService tradeOfferService;

    /**
     * DELETE request handler.
     * provides an endpoint to {@code '/api/v1/trades/<id>/<id>'} through which an {student} may delete his {@link TradeOffer}.
     *
     * @param studentId studentId of requester
     * @param tradeId   tradeId of tradeoffer which is to be deleted.
     * @return {@link HttpStatus#OK} if tradeoffer was deleted, {@link HttpStatus#NOT_FOUND} if tradeoffer wasnt found,
     * {@link HttpStatus#BAD_REQUEST} if requester isnt owner of the tradeoffer.
     */
    @DeleteMapping("/{studentId}/{tradeId}")
    public ResponseEntity deleteTradeOffer(@PathVariable("studentId") long studentId, @PathVariable("tradeId") long tradeId) {
        log.info(String.format("DELETE Request Student: %d TradeOffer: %d", studentId, tradeId));
        try {
            if (tradeOfferService.deleteTradeOffer(studentId, tradeId)) {
                log.info(String.format("DELETE Request successful Student: %d TradeOffer: %d", studentId, tradeId));
                return new ResponseEntity(HttpStatus.OK);
            }
        } catch (TradeOfferNotFoundException ex) {
            log.info(String.format("ERROR while DELETE Request Student: %d TradeOffer: %d - Entity not found", studentId, tradeId));
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        log.info(String.format("ERROR while DELETE Request Student: %d TradeOffer: %d - Student isn't owner of entity", studentId, tradeId));
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    /**
     * POST request handler.
     * provides an endpoint to {@code '/api/v1/trades/<id>/<id>/<id>} through which an student may insert a new {@link TradeOffer}
     *
     * @param studentId id of requester
     * @param offerId   id of timeslot which requester is offering
     * @param seekId    id of sought timeslot
     * @return {@link HttpStatus#OK} plus the new {@link TradeOffer} instance which has been created,
     * {@link HttpStatus#INTERNAL_SERVER_ERROR} if the server encountered an error.
     */
    @PostMapping("/{studentId}/{offer}/{seek}")
    public ResponseEntity<TradeOffer> createTradeOffer(@PathVariable("studentId") long studentId,
                                                       @PathVariable("offer") long offerId,
                                                       @PathVariable("seek") long seekId) {
        log.info(String.format("POST Request Student: %d with Offer/Seek: %d/%d", studentId, offerId, seekId));
        try {
            var tradeoffer = tradeOfferService.createTradeOffer(studentId, offerId, seekId);
            log.info(String.format("SUCCESS POST Request Student: %d with Offer/Seek: %d/%d - insert successful", studentId, offerId, seekId));
            return new ResponseEntity<>(tradeoffer, HttpStatus.OK);
        } catch (NotFoundException ex) {
            log.info(String.format("ERROR POST Request Student: %d with Offer/Seek: %d/%d - error while creating entity", studentId, offerId, seekId));
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * POST request handler.
     * provides an endpoint to {@code '/api/v1/trades/<id>/<id>/accept} through which an student may accept a given {@link TradeOffer}
     *
     * @param studentId id of requester
     * @param tradeId id of trade which requester wants to accept
     * @return {@link HttpStatus#OK} if successful, {@link HttpStatus#BAD_REQUEST} if trade transaction failed,
     * {@link HttpStatus#INTERNAL_SERVER_ERROR} if an runtime exception was thrown by the service
     */
    @PostMapping("/{studentId}/{tradeId}/accept")
    public ResponseEntity acceptTrade(@PathVariable("studentId") long studentId,
                                      @PathVariable("tradeId") long tradeId,
                                      @RequestBody TradeRequest tradeRequest,
                                      BindingResult result) {
        log.info(String.format("POST Request Accept Trade: %d Requester: %d", tradeId, studentId));
        if(result.hasErrors()){
            log.info(String.format("Post Request error, trade: %d from requester: %d failed", tradeId, studentId));
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        try {
            if (tradeOfferService.tradeTimeslots(studentId, tradeId)) {
                log.info(String.format("POST Request successful, performed trade: %d Requester: %d", tradeId, studentId));
                return new ResponseEntity(HttpStatus.OK);
            }
            log.info(String.format("Post Request error, trade: %d from requester: %d failed", tradeId, studentId));
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } catch (RuntimeException ex) {
            log.info(String.format("POST Request error, trade: %d from requester: %d - internal error", tradeId, studentId));
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
