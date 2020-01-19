package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.authentication.LoginRequestBody;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import de.hsrm.mi.swtp.exchangeplatform.model.data.UserType;
import de.hsrm.mi.swtp.exchangeplatform.model.data.enums.TypeOfUsers;
import de.hsrm.mi.swtp.exchangeplatform.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class BasicTradeServiceTest {
	
	@Autowired
	private BasicTradeService service;
	private UserRepository repository;
	@Test
	public void testDoTradeInvalidStudentIdThrowsNotFoundException() {
		assertThrows(NotFoundException.class, () -> {
			service.doTrade(9999, 999, 999);
		});
	}
	
	@Test
	public void testDoTradeInvalidOfferedIdThrowsNotFoundException() {
		
		List<User> users = repository.findAll();
		
		User student = null;
		for(User user: users) {
			if(user.getUserType().getType() == TypeOfUsers.STUDENT) {
				student = user;
			}
		}
		
		if(student == null) fail("No Student in Database");
		
		long validStudentId = student.getId();
		
		assertThrows(NotFoundException.class, () -> {
			service.doTrade(validStudentId, 9999, 99);
		});
	}

}
