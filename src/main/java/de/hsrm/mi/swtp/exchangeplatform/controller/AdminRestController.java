package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.rest.AdminSettingsRequest;
import de.hsrm.mi.swtp.exchangeplatform.model.settings.AdminSettings;
import de.hsrm.mi.swtp.exchangeplatform.service.settings.AdminSettingsService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import javax.jms.JMSException;

/**
 * A rest controller which handles getting and updating {@link AdminSettings}
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@SecurityRequirement(name = "Authorization")
@RequestMapping("/api/v1/admin")
public class AdminRestController {
	
	AdminSettingsService adminSettingsService;
	
	/**
	 * POST request handler
	 * <p>
	 * provides an endpoint for admins to update {@link AdminSettings}
	 *
	 * @param adminSettingsRequest request containing admin settings
	 * @param bindingResult
	 *
	 * @return new admin settings if successful
	 */
	@PostMapping("/settings")
	@ApiOperation(value = "update admin settings", nickname = "updateAdminSettings")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "successfully updated settings"),
							@ApiResponse(code = 403, message = "unauthorized update settings attempt"),
							@ApiResponse(code = 400, message = "malformed admin settings request") })
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<AdminSettings> updateAdminSettings(@RequestBody AdminSettingsRequest adminSettingsRequest, BindingResult bindingResult) throws
			NotFoundException {
		if(bindingResult.hasErrors()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		if(adminSettingsService.updateAdminSettings(adminSettingsRequest.isTradesActive(), adminSettingsRequest.getActiveFilters()))
			return new ResponseEntity<>(adminSettingsService.getAdminSettings(), HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/settings")
	@ApiOperation(value = "get admin settings", nickname = "getAdminSettings")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "successfully received adminsettings"),
							@ApiResponse(code = 403, message = "unauthorized get settings attempt") })
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<AdminSettings> getAdminSettings() {
		return new ResponseEntity<>(adminSettingsService.getAdminSettings(), HttpStatus.OK);
	}
	
	@GetMapping("/tradingActive")
	@ApiOperation(value = "get trading active", nickname = "getTradingActive")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "successfully received adminsettings"),
							@ApiResponse(code = 403, message = "unauthorized get settings attempt") })
	public ResponseEntity<Boolean> getTradingActive() {return new ResponseEntity<>(adminSettingsService.isTradesActive(), HttpStatus.OK); }
	
}
