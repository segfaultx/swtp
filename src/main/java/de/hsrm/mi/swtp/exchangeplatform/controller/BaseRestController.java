package de.hsrm.mi.swtp.exchangeplatform.controller;

import de.hsrm.mi.swtp.exchangeplatform.model.data.Model;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface BaseRestController<T extends Model, Id> {
	
	/**
	 * GET request handler.
	 *
	 * @return a JSON made up of a list containing all available {@link T} instances.
	 * If there are none will return an empty list.
	 */
	@GetMapping
	ResponseEntity<List<T>> getAll();
	
	/**
	 * GET request handler.
	 *
	 * @param id is the id of a {@link T}.
	 *
	 * @return {@link HttpStatus#OK} and the requested {@link T} instance if it is found. Otherwise will return {@link HttpStatus#BAD_REQUEST}
	 */
	@GetMapping("/{id}")
	ResponseEntity<T> getById(Id id);
	
	/**
	 * POST request handler.
	 * Provides an endpoint through which an {admin} may create a new {@link T}.
	 *
	 * @param model a new {@link T} which is to be created and inserted into the database if not existing already.
	 *
	 * @return {@link HttpStatus#OK} and the created timeslot if was created successfully. Otherwise will return {@link HttpStatus#BAD_REQUEST}.
	 */
	@PostMapping
	ResponseEntity<T> create(T model, BindingResult result);
	
	/**
	 * PATCH request handler.
	 * Provides an endpoint through which a {@link T} can be updated.
	 *
	 * @param id    is the identifier of the {@link T} which is to be updated/modified.
	 * @param model is an object which contains the data of an the updated {@link T}.
	 *
	 * @return {@link HttpStatus#OK} and the updated timeslot if the timeslot was updated successfully. Otherwise will return {@link HttpStatus#BAD_REQUEST}.
	 */
	@PatchMapping("/{id}")
	ResponseEntity<T> update(@PathVariable Id id, @RequestBody T model, BindingResult result);
	
	/**
	 * DELETE request handler.
	 * Provides an endpoint through which an {admin} may delete a {@link T}.
	 *
	 * @param id the id of a {@link T} which is to be deleted.
	 *
	 * @return {@link HttpStatus#OK} if the {@link T} was removed successfully. Otherwise will return {@link HttpStatus#BAD_REQUEST}.
	 */
	@DeleteMapping("/admin/{id}")
	ResponseEntity<T> delete(@PathVariable Id id);
	
}
