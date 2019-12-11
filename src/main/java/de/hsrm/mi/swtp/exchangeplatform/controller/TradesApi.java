/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (4.2.1).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.model.rest_models.TimetableDTO;
import de.hsrm.mi.swtp.exchangeplatform.model.rest_models.TradeOfferDTO;
import de.hsrm.mi.swtp.exchangeplatform.model.rest_models.TradeRequest;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-12-11T08:15:17.638366+01:00[Europe/Berlin]")

@Validated
@Api(value = "trades", description = "the trades API")
public interface TradesApi {

    @ApiOperation(value = "Create Trade Offer.", nickname = "createTradeOffer", notes = "", response = TradeOfferDTO.class, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "The new trade offer", response = TradeOfferDTO.class),
        @ApiResponse(code = 400, message = "Bad Request") })
    @RequestMapping(value = "/trades/offers",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<TradeOfferDTO> createTradeOffer(@ApiParam(value = "" ,required=true )  @Valid @RequestBody TradeRequest tradeRequest);


    @ApiOperation(value = "Request Trade.", nickname = "requestTrade", notes = "", response = TimetableDTO.class, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "The new timetable", response = TimetableDTO.class),
        @ApiResponse(code = 400, message = "Bad Request") })
    @RequestMapping(value = "/trades",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<TimetableDTO> requestTrade(@ApiParam(value = "" ,required=true )  @Valid @RequestBody TradeRequest tradeRequest);

}
