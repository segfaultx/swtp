package de.hsrm.mi.swtp.exchangeplatform.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AppointmentRequestBody {
    
    @JsonProperty("appointment_id")
    Long appointmentId;
    
    @JsonProperty("matr_nr")
    Long matriculationNumber;
    
}
