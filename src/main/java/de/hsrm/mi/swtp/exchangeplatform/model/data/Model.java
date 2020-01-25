package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

//TODO: javadoc
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler"})
public interface Model extends Serializable {
	Long getId();
}
