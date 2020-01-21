package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.repository.UserRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class BasicTradeServiceTest {
	
	@Autowired
	private BasicTradeService service;
	
	@Autowired
	private UserRepository repository;
	
	// TODO: Write tests
//	@Test
//	public void testDoTradeInvalidStudentIdThrowsNotFoundException() {
//		assertThrows(NotFoundException.class, () -> {
//			service.doTrade(9999, 999, 999);
//		});
//	}
//
//	@Test
//	public void testDoTradeInvalidOfferedIdThrowsNotFoundException() {
//
//		List<User> users = repository.findAll();
//
//		User student = null;
//		for(User user: users) {
//			if(user.getUserType().getType() == TypeOfUsers.STUDENT) {
//				student = user;
//				break;
//			}
//		}
//
//		if(student == null) fail("No Student in Database");
//
//		long validStudentId = student.getId();
//
//		assertThrows(NotFoundException.class, () -> {
//			service.doTrade(validStudentId, 9999, 99);
//		});
//	}

}
