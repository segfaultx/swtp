package de.hsrm.mi.swtp;

import org.junit.jupiter.api.Test;
import org.python.core.PyList;
import org.python.util.PythonInterpreter;
import org.springframework.boot.test.context.SpringBootTest;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import org.springframework.test.annotation.DirtiesContext;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import java.util.ArrayList;
import java.util.Arrays;

import static org.springframework.test.util.AssertionErrors.assertEquals;
@SpringBootTest
@DirtiesContext
class SwtpApplicationTests {
	
	@Test
	void contextLoads() {
	}
	
	@Test
	void jythonTest() {
		PythonInterpreter myCoolInterpreter = new PythonInterpreter();
		
		myCoolInterpreter.exec("from de.hsrm.mi.swtp.exchangeplatform.model.data import User");
		myCoolInterpreter.exec("print 'hallo'");
		myCoolInterpreter.exec("user = User()");
		myCoolInterpreter.exec("print user");
	}
	@Test
	void ScriptEngineTest() throws Exception{
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine py = manager.getEngineByName("python");
		py.eval("lst = [1,2,3,4]");
		PyList returnVal = (PyList) py.get("lst");
		var iter = returnVal.iterator();
		ArrayList<Integer> out = new ArrayList<>();
		while(iter.hasNext()){
			Integer item = (Integer) iter.next();
			out.add(item);
		}
		// TODO: result klasse erstellen
		System.out.print(out);
		assertEquals("assert equals",returnVal, 15);
	}
	
}
