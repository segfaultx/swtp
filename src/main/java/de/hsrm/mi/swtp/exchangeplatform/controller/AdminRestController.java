package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.model.rest.AdminSettingsRequest;
import de.hsrm.mi.swtp.exchangeplatform.model.settings.AdminSettings;
import de.hsrm.mi.swtp.exchangeplatform.service.settings.AdminSettingsService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("/api/v1/admin")
public class AdminRestController {
	
	AdminSettingsService adminSettingsService;
	
	
	@PostMapping("/settings")
	@ApiOperation(value = "update admin settings", nickname = "updateAdminSettings")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "successfully updated settings"),
							@ApiResponse(code = 403, message = "unauthorized update settings attempt"),
							@ApiResponse(code = 400, message = "malformed admin settings request") })
	public ResponseEntity<AdminSettings> updateAdminSettings(@RequestBody AdminSettingsRequest adminSettingsRequest, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		if(adminSettingsService.updateAdminSettings(adminSettingsRequest.isTradesActive(), adminSettingsRequest.getActiveFilters()))
			return new ResponseEntity<>(adminSettingsService.getAdminSettings(), HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/settings")
	@ApiOperation(value = "get admin settings", nickname = "getAdminSettings")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "successfully received adminsettings"),
							@ApiResponse(code = 403, message = "unauthorized get settings attempt") })
	public ResponseEntity<AdminSettings> getAdminSettings() {
		return new ResponseEntity<>(adminSettingsService.getAdminSettings(), HttpStatus.OK);
	}
}
