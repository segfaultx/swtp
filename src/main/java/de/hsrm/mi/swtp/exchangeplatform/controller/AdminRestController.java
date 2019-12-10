package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.model.rest_models.AdminSettingsRequest;
import de.hsrm.mi.swtp.exchangeplatform.model.settings.AdminSettings;
import de.hsrm.mi.swtp.exchangeplatform.service.settings.AdminSettingsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level =  AccessLevel.PRIVATE)
public class AdminRestController implements AdminApi {
	
	AdminSettingsService adminSettingsService;
	
	
	@Override
	public ResponseEntity<Void> updateAdminSettings(@Valid AdminSettingsRequest adminSettingsRequest) {
		if (adminSettingsService.updateAdminSettings(adminSettingsRequest.getTradesActive(), adminSettingsRequest.getActiveFilters()))
			return new ResponseEntity<>(HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
}
