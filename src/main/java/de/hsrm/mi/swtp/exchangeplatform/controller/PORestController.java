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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.JMSException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@SecurityRequirement(name = "Authorization")
@RestController
@RequestMapping("/api/v1/pos")
public class PORestController {
	
	String BASEURL = "/api/v1/pos";
	POService poService;
	
	@GetMapping
	@Operation(description = "get all POs", operationId = "getAllPOs")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successfully retrieved POs"),
							@ApiResponse(responseCode = "403", description = "unauthorized fetch attempt"),
							@ApiResponse(responseCode = "400", description = "malformed fetch request") })
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<PO>> getAll() {
		log.info("GET // " + BASEURL);
		return ResponseEntity.ok(poService.getAll());
	}
	
	@GetMapping("/{poId}")
	@Operation(description = "get po by id", operationId = "getPOById")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successfully retrieved POs"),
							@ApiResponse(responseCode = "403", description = "unauthorized fetch attempt"),
							@ApiResponse(responseCode = "400", description = "malformed fetch request") })
	@PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
	public ResponseEntity<PO> getById(@PathVariable Long poId) throws NotFoundException, JMSException {
		log.info(String.format("GET // " + BASEURL + "/%s", poId));
		PO po = poService.getById(poId).orElseThrow(NotFoundException::new);
		return ResponseEntity.ok(po);
	}
	
}
