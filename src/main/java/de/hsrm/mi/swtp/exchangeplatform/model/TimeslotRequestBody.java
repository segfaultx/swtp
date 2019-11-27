package de.hsrm.mi.swtp.exchangeplatform.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TimeslotRequestBody {

    @JsonProperty("timeslot_id")
    Long timeslotId;
    
    @JsonProperty("matr_nr")
    Long matriculationNumber;
    
}
