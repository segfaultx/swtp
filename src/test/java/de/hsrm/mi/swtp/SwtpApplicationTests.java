package de.hsrm.mi.swtp;

import org.junit.jupiter.api.Test;
import org.python.util.PythonInterpreter;
import org.springframework.boot.test.context.SpringBootTest;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;

@SpringBootTest
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
	
}
