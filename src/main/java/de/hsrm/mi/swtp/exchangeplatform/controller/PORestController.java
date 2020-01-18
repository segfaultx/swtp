package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.PO;
import de.hsrm.mi.swtp.exchangeplatform.service.rest.POService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@SecurityRequirement(name = "Authorization")
@RestController
@RequestMapping("/api/v1/pos")
@PreAuthorize("hasRole('ADMIN')")
public class PORestController {
	
	String BASEURL = "/api/v1/pos";
	POService poService;
	
	@GetMapping
	@Operation(description = "get all POs", operationId = "getAllPOs")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successfully retrieved POs"),
							@ApiResponse(responseCode = "403", description = "unauthorized fetch attempt"),
							@ApiResponse(responseCode = "400", description = "malformed fetch request") })
	public ResponseEntity<List<PO>> getAll() {
		log.info("GET // " + BASEURL);
		return ResponseEntity.ok(poService.getAll());
	}
	
	@GetMapping("/{poId}")
	@Operation(description = "get po by id", operationId = "getPOById")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successfully retrieved POs"),
							@ApiResponse(responseCode = "403", description = "unauthorized fetch attempt"),
							@ApiResponse(responseCode = "400", description = "malformed fetch request") })
	
	public ResponseEntity<PO> getById(@PathVariable Long poId) throws NotFoundException {
		return ResponseEntity.ok(poService.getById(poId));
	}
	
	@PutMapping
	@Operation(description = "update a po", operationId = "updatePO")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successfully updated PO"),
							@ApiResponse(responseCode = "403", description = "unauthorized update attempt"),
							@ApiResponse(responseCode = "400", description = "malformed update request"),
							@ApiResponse(responseCode = "409", description = "the exchangeplatform is still active. it needs to be inactive before updating any po settings.")
	})
	public ResponseEntity<PO> updatePOById(@RequestBody(required = true) @NonNull final PO po) throws NotFoundException {
		poService.update(po);
		return ResponseEntity.ok(poService.getById(po.getId()));
	}
	
}
