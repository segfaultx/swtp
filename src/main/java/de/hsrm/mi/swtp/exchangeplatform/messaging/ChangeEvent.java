package de.hsrm.mi.swtp.exchangeplatform.messaging;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChangeEvent {

    @JsonProperty(value = "id")
    String id;

    String type = "CHANGE";

}
