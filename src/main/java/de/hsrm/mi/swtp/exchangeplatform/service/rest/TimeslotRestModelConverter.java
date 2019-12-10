package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Timeslot;
import de.hsrm.mi.swtp.exchangeplatform.model.rest_models.*;
import de.hsrm.mi.swtp.exchangeplatform.model.rest_models.Module;
import de.hsrm.mi.swtp.exchangeplatform.repository.TimeslotRepository;
import de.hsrm.mi.swtp.exchangeplatform.repository.TradeOfferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class TimeslotRestModelConverter implements RestModelConverter {
	
	TradeOfferService tradeOfferService;
	TradeOfferRepository tradeOfferRepository;
	TimeslotRepository timeslotRepository;
	Map<String, String> dayMapper = new HashMap<>();
	Map<String, String> timeslotTypeMapper = new HashMap<>();
	
	@Autowired
	public TimeslotRestModelConverter(@NotNull TradeOfferService tradeOfferService, @NotNull TradeOfferRepository tradeOfferRepository,
									  @NotNull TimeslotRepository timeslotRepository
									 ) {
		this.tradeOfferService = tradeOfferService;
		this.tradeOfferRepository = tradeOfferRepository;
		this.timeslotRepository = timeslotRepository;
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
	public Object convertToRest(Object object) {
		Timeslot timeslot = (Timeslot) object;
		de.hsrm.mi.swtp.exchangeplatform.model.rest_models.Timeslot out = new de.hsrm.mi.swtp.exchangeplatform.model.rest_models.Timeslot();
		out.setDay(DayEnum.fromValue(dayMapper.get(timeslot.getDay())));
		
		Room room = new Room();
		room.setRoomNumber(timeslot.getRoom().getRoomNumber());
		room.setLocation(timeslot.getRoom().getLocation());
		room.setId(timeslot.getRoom().getId());
		out.setRoom(room);
		
		Lecturer lecturer = new Lecturer();
		lecturer.setName(timeslot.getLecturer().getLastName());
		lecturer.setMail(timeslot.getLecturer().getEmail());
		out.setLecturer(lecturer);
		
		out.setCapacity(timeslot.getCapacity());
		
		out.setId(timeslot.getId());
		
		Module module = new Module();
		module.setName(timeslot.getModule().getName());
		module.setId(timeslot.getModule().getId());
		out.setModule(module);
		out.setType(TimeslotType.fromValue(timeslotTypeMapper.get(timeslot.getType())));
		
		out.setTimeStart(Timestamp.valueOf(LocalDateTime.of(LocalDate.now(), timeslot.getTimeStart())));
		out.setTimeEnd(Timestamp.valueOf(LocalDateTime.of(LocalDate.now(), timeslot.getTimeEnd())));
		
		PossibleTradesResponse possibleTradesResponse = new PossibleTradesResponse();
		var instantTrades = tradeOfferRepository.findAllBySeekAndInstantTrade(timeslot, true);
		instantTrades.forEach(tradeOffer -> possibleTradesResponse.addInstantItem(tradeOffer.getOffer().getId()));
		var regularTrades = tradeOfferRepository.findAllBySeekAndInstantTrade(timeslot, false);
		regularTrades.forEach(tradeOffer -> possibleTradesResponse.addTradesAvailableItem((tradeOffer.getOffer().getId())));
		/*
		var others = timeslotRepository.findAllByModule(timeslot.getModule());
		if(others != null) {
			for(Timeslot timeslt: others){
				System.out.println(timeslt.getId());
			}
			others.forEach(timeslt -> {
				if(!possibleTradesResponse.getInstant().contains(timeslt.getId()) && !possibleTradesResponse.getTradesAvailable().contains(timeslt.getId()))
					possibleTradesResponse.addRemainingItem(timeslt.getId());
			});
		}
		 */
		out.setPossibleTrades(possibleTradesResponse);
		
		//TODO: fix stackoverflow errors of objects
		return out;
	}
}
