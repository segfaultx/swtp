package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Model;

import java.util.List;

/**
 * @author Henock
 * @param <T>
 * @param <Id>
 */
public interface RestService<T extends Model, Id> {
	/**
	 *
	 * @return
	 */
	List<T> getAll();
	
	/**
	 *
	 * @param id
	 * @return
	 * @throws NotFoundException
	 */
	T getById(Id id) throws NotFoundException;
	
	/**
	 *
	 * @param item
	 * @throws IllegalArgumentException
	 */
	void save(T item) throws IllegalArgumentException;
	
}
