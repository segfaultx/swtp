package de.hsrm.mi.swtp.exchangeplatform.service.rest;


import de.hsrm.mi.swtp.exchangeplatform.model.data.Module;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ModuleServiceTest {
	
	@Autowired
	private ModuleService moduleService;
	
	@Test
	public void testSaveNewModule() {
		Long randomUniqueId = UUID.randomUUID().getLeastSignificantBits();
		Module module = new Module();
		module.setId(randomUniqueId);
		Module saved = moduleService.save(module);
		
		// cleanup again
		moduleService.delete(module);
		assertEquals(saved.getId(), randomUniqueId);
	}
}
