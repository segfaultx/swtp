package de.hsrm.mi.swtp.exchangeplatform.service.filter.TradeFilter;

import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.Filter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.List;

/**
 * Custom Python Filter entity class
 * @author amatus
 */
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Getter
public class CustomPythonFilter implements Filter {
	
	@Id
	@GeneratedValue
	Long id;
	
	String filterName;
	@Lob
	String pythonCode;
	
	public CustomPythonFilter(String filterName, String code){
		this.filterName = filterName;
		this.pythonCode = code;
	}
	
	
	@Override
	public List<TradeOffer> doFilter(List<TradeOffer> offers, User seeker) throws RuntimeException {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine py = manager.getEngineByName("python");
		py.put("offers", offers);
		py.put("seeker", seeker);
		try{
			py.eval(pythonCode);
		}catch(ScriptException ex){
			ex.printStackTrace();
			throw new RuntimeException("Error processing the python script");
		}
		return offers;
	}
	
	@Override
	public String getFilterName() {
		return filterName;
	}
}
