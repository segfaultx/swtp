package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.admin.settings.AdminSettings;
import de.hsrm.mi.swtp.exchangeplatform.model.data.TradeOffer;
import de.hsrm.mi.swtp.exchangeplatform.model.rest.AdminSettingsRequest;
import de.hsrm.mi.swtp.exchangeplatform.model.rest.CustomPythonFilterRequest;
import de.hsrm.mi.swtp.exchangeplatform.service.filter.TradeFilter.CustomPythonFilter;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.TradeOfferService;
import de.hsrm.mi.swtp.exchangeplatform.service.settings.AdminSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
	TradeOfferService tradeOfferService;
	
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
	@Operation(description = "update admin settings", operationId = "updateAdminSettings", tags = {"admin"})
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successfully updated settings"),
							@ApiResponse(responseCode = "403", description = "unauthorized update settings attempt"),
							@ApiResponse(responseCode = "400", description = "malformed admin settings request") })
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<AdminSettings> updateAdminSettings(@RequestBody AdminSettingsRequest adminSettingsRequest, BindingResult bindingResult) throws
			NotFoundException, ClassNotFoundException {
		if(bindingResult.hasErrors()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		if(adminSettingsService.updateAdminSettings(adminSettingsRequest.isTradesActive(),
													adminSettingsRequest.getActiveFilters(),
													adminSettingsRequest.getDateStartTrades(),
													adminSettingsRequest.getDateEndTrades()))
			return new ResponseEntity<>(adminSettingsService.getAdminSettings(), HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	
	/**
	 * GET request handler
	 *
	 * provides an endpoint to {@link de.hsrm.mi.swtp.exchangeplatform.model.data.User} admins to get the current admin settings
	 * @return admin settings containing trades active flag + current active filters
	 */
	@GetMapping("/settings")
	@Operation(description = "get admin settings", operationId = "getAdminSettings", tags = {"admin"})
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successfully received adminsettings"),
							@ApiResponse(responseCode = "403", description = "unauthorized get settings attempt") })
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<AdminSettings> getAdminSettings() {
		return new ResponseEntity<>(adminSettingsService.getAdminSettings(), HttpStatus.OK);
	}
	
	/**
	 * GET request handler
	 *
	 * provides an endpoint to {@link de.hsrm.mi.swtp.exchangeplatform.model.data.User} admins to get the value of the trades active flag
	 * @return bool value to indicate wether trades are active or not
	 */
	@GetMapping("/tradingActive")
	@Operation(description = "get trading active", operationId = "getTradingActive", tags = {"admin"})
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successfully received adminsettings"),
							@ApiResponse(responseCode = "403", description = "unauthorized get settings attempt") })
	public ResponseEntity<Boolean> getTradingActive() {
		return new ResponseEntity<>(adminSettingsService.isTradesActive(), HttpStatus.OK);
	}
	
	
	/**
	 * GET request handler
	 *
	 * provides an endpoint to {@link de.hsrm.mi.swtp.exchangeplatform.model.data.User} admins to fetch all available filters
	 * @return list of strings containing filter names
	 */
	@GetMapping("/settings/filters")
	@Operation(description = "get all available filters", operationId = "getAllAvailableFilters", tags = {"admin"})
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successfully fetched all filters"),
							@ApiResponse(responseCode = "403", description = "unauthorized request"),
							@ApiResponse(responseCode = "400", description = "malformed request")
	})
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<String>> getAllAvailableFilters(){
		return new ResponseEntity<>(adminSettingsService.getAllFilters(), HttpStatus.OK);
	}
	
	/**
	 * POST request handler
	 *
	 * provides an endpoint for {@link de.hsrm.mi.swtp.exchangeplatform.model.data.User} admins to create a custom filter
	 * @return custom filter
	 */
	@PostMapping("/customfilters")
	@Operation(description = "create a custom python filter", operationId = "createCustomFilter", tags = {"admin"})
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successfully created custom filter"),
							@ApiResponse(responseCode = "403", description = "unauthorized request"),
							@ApiResponse(responseCode = "400", description = "malformed request")
	})
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CustomPythonFilter> createCustomPythonFilter(@RequestBody CustomPythonFilterRequest customPythonFilterRequest){
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine py = manager.getEngineByName("python");
		var offers = new ArrayList<TradeOffer>();
		var testOffer = tradeOfferService.getAll().stream().findFirst().orElseThrow();
		offers.add(testOffer);
		
		// validate python filter can be run
		// HUGE SECURITY RISK
		try {
			py.put("offers", offers);
			py.eval(customPythonFilterRequest.getCode());
		} catch(ScriptException ex){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(adminSettingsService.addCustomPythonFilter(customPythonFilterRequest.getFilterName(),
																			   customPythonFilterRequest.getCode()),
									HttpStatus.OK);
	}
	
	/**
	 * GET request handler
	 *
	 * provides an endpoint to {@link de.hsrm.mi.swtp.exchangeplatform.model.data.User} admins to get the template script
	 * for custom python scripts
	 * @return custom python template string
	 */
	@GetMapping("/filtertemplate")
	@Operation(description = "get the custom filter template", operationId = "getCustomFilterTemplate", tags = {"admin"})
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successfully retrieved custom filter template"),
							@ApiResponse(responseCode = "403", description = "unauthorized request"),
							@ApiResponse(responseCode = "500", description = "error fetching the default script template")
	})
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> getDefaultPythonString(){
		try{
			Resource resource = new ClassPathResource("Beispielskript.py");
			StringBuilder builder = new StringBuilder();
			InputStream inputStream = resource.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			String line = reader.readLine();
			while(line != null){
				builder.append(line);
				builder.append('\n'); // preserve line breaks
				line = reader.readLine();
			}
			return new ResponseEntity<>(builder.toString(), HttpStatus.OK);
		}catch(IOException ex){
			ex.printStackTrace();
			throw new RuntimeException("Error reading Beispielskript.py");
		}
	}

}
