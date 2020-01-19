package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.ExchangeplatformStillActiveException;
import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.PO;
import de.hsrm.mi.swtp.exchangeplatform.service.admin.po.POUpdateService;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.POService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SecurityRequirement(name = "Authorization")
@RequestMapping("/api/v1/pos")
@Slf4j
public class PORestController {

	String BASEURL = "/api/v1/pos";
	POService poService;
	POUpdateService poUpdateService;

	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(description = "get all POs", operationId = "getAllPOs")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successfully retrieved POs"),
							@ApiResponse(responseCode = "403", description = "unauthorized fetch attempt"),
							@ApiResponse(responseCode = "400", description = "malformed fetch request") })
	public ResponseEntity<List<PO>> getAll() {
		log.info("GET // " + BASEURL);
		return ResponseEntity.ok(poService.getAll());
	}

	@GetMapping("/{poId}")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(description = "get po by id", operationId = "getPOById")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successfully retrieved POs"),
							@ApiResponse(responseCode = "403", description = "unauthorized fetch attempt"),
							@ApiResponse(responseCode = "400", description = "malformed fetch request") })

	public ResponseEntity<PO> getById(@PathVariable Long poId) throws NotFoundException {
		return ResponseEntity.ok(poService.getById(poId));
	}

	@PutMapping(consumes = { MediaType.APPLICATION_JSON_VALUE })
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(description = "update a po", operationId = "updatePO")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successfully updated PO"),
							@ApiResponse(responseCode = "403", description = "unauthorized update attempt"),
							@ApiResponse(responseCode = "400", description = "malformed update request"),
							@ApiResponse(responseCode = "409", description = "the exchangeplatform is still active. it needs to be inactive before updating any po settings.") })
	public ResponseEntity<PO> updatePOById(@RequestBody PO po, BindingResult bindingResult) throws NotFoundException, ExchangeplatformStillActiveException {
		// will only perform update if changes to restrictions are existent
		poUpdateService.update(po);
		return ResponseEntity.ok(poService.getById(po.getId()));
	}

}
