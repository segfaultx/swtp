package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.rest_models.*;
import de.hsrm.mi.swtp.exchangeplatform.repository.TimeslotRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.TradeOfferRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class TimeslotRestModelConverter implements RestModelConverter<TimeslotDTO> {
	
	TradeOfferService tradeOfferService;
	TradeOfferRepository tradeOfferRepository;
	TimeslotRepository timeslotRepository;
	RoomRestModelConverter roomRestModelConverter;
	ModuleRestConverter moduleRestConverter;
	LecturerRestModelConverter lecturerRestModelConverter;
	Map<String, String> dayMapper = new HashMap<>();
	Map<String, String> timeslotTypeMapper = new HashMap<>();
	
	@Autowired
	public TimeslotRestModelConverter(@NotNull TradeOfferService tradeOfferService, @NotNull TradeOfferRepository tradeOfferRepository,
									  @NotNull TimeslotRepository timeslotRepository, @NotNull RoomRestModelConverter roomRestModelConverter,
									  @NotNull LecturerRestModelConverter lecturerRestModelConverter, @NonNull ModuleRestConverter moduleRestConverter
									 ) {
		this.tradeOfferService = tradeOfferService;
		this.tradeOfferRepository = tradeOfferRepository;
		this.timeslotRepository = timeslotRepository;
		this.roomRestModelConverter = roomRestModelConverter;
		this.lecturerRestModelConverter = lecturerRestModelConverter;
		this.moduleRestConverter = moduleRestConverter;
		this.dayMapper.put("Mittwoch", "wednesday");
		this.dayMapper.put("Montag", "monday");
		this.dayMapper.put("Dienstag", "tuesday");
		this.dayMapper.put("Donnerstag", "thursday");
		this.dayMapper.put("Freitag", "friday");
		this.dayMapper.put("Samstag", "saturday");
		this.dayMapper.put("Sonntag", "sunday");
		this.timeslotTypeMapper.put("PRAKTIKUM", "PracticalTraining");
		this.timeslotTypeMapper.put("VORLESUNG", "Lecture");
		this.timeslotTypeMapper.put("ÜBUNG", "Exercise");
		
	}
	
	@Override
	public boolean isResponsible(Object object) {
		return object instanceof Timeslot;
	}
	
	@Override
	public TimeslotDTO convertToRest(Object object) {
		Timeslot timeslot = (Timeslot) object;
		TimeslotDTO out = new TimeslotDTO();
		
		out.setDay(DayEnum.fromValue(dayMapper.get(timeslot.getDay())));
		
		out.setRoom(roomRestModelConverter.convertToRest(timeslot.getRoom()));
		
		out.setLecturer(lecturerRestModelConverter.convertToRest(timeslot.getLecturer()));
		
		out.setCapacity(JsonNullable.of(timeslot.getCapacity()));
		
		out.setId(timeslot.getId());
		
		out.setModule(moduleRestConverter.convertToRest(timeslot.getModule()));
		
		out.setType(TimeslotType.fromValue(timeslotTypeMapper.get(timeslot.getType())));
		
		out.setTimeStart(OffsetDateTime.of(LocalDate.now(), timeslot.getTimeStart(), ZoneOffset.UTC));
		out.setTimeEnd(OffsetDateTime.of(LocalDate.now(), timeslot.getTimeEnd(), ZoneOffset.UTC));
		
		PossibleTradesResponse possibleTradesResponse = new PossibleTradesResponse();
		possibleTradesResponse.setInstant(new ArrayList<>());
		possibleTradesResponse.setRemaining(new ArrayList<>());
		possibleTradesResponse.setTradesAvailable(new ArrayList<>());
		var allTrades = tradeOfferRepository.findAllBySeek(timeslot);
		if(allTrades == null) allTrades = new ArrayList<>();
		allTrades.forEach(tradeOffer -> {
			if(tradeOffer.isInstantTrade()) possibleTradesResponse.addInstantItem(tradeOffer.getOffer().getId());
		});
		allTrades.forEach(tradeOffer -> {
			if(!tradeOffer.isInstantTrade()) possibleTradesResponse.addTradesAvailableItem((tradeOffer.getOffer().getId()));
		});
		var others = timeslotRepository.findAllByModule(timeslot.getModule());
		if(others != null) {
			others.forEach(timeslt -> {
				if(!possibleTradesResponse.getInstant().contains(timeslt.getId()) && !possibleTradesResponse.getTradesAvailable().contains(timeslt.getId()))
					possibleTradesResponse.addRemainingItem(timeslt.getId());
			});
		}
		
		out.setPossibleTrades(possibleTradesResponse);
		
		//TODO: fix stackoverflow errors of objects
		return out;
	}
}
