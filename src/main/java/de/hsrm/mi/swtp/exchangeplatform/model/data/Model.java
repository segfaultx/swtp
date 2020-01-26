package de.hsrm.mi.swtp.exchangeplatform.model.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * @author Henock
 */
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler"})
public interface Model extends Serializable {
	Long getId();
}
