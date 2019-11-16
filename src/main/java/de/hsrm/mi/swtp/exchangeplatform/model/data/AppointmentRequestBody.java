package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AppointmentRequestBody implements RequestBody {

    @JsonProperty("appointment_id")
    Long appointmentId;

    @JsonProperty("student")
    Student student;

}
