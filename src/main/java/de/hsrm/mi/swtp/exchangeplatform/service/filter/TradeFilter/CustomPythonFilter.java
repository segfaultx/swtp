package de.hsrm.mi.swtp.exchangeplatform.service.filter.TradeFilter;

import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.Filter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.python.util.PythonInterpreter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
public class CustomPythonFilter implements Filter {
	
	@Id
	@GeneratedValue
	Long id;
	
	String filterName;
	
	String pythonCode;
	
	public CustomPythonFilter(String filterName, String code){
		this.filterName = filterName;
		this.pythonCode = code;
	}
	
	
	@Override
	public List<TradeOffer> doFilter(List<TradeOffer> offers) throws RuntimeException {
		// TODO: execute jython code here
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine py = manager.getEngineByName("python");
		try{
			var returnVal = py.eval("5*3");
			System.out.print(returnVal);
		}catch(ScriptException ex){
			ex.printStackTrace();
			throw new RuntimeException("Error while Executing Script");
		}
		return null;
	}
	
	@Override
	public String getFilterName() {
		return filterName;
	}
}
