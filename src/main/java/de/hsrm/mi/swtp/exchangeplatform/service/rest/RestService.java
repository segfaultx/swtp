package de.hsrm.mi.swtp.exchangeplatform.service.rest;

import de.hsrm.mi.swtp.exchangeplatform.exceptions.notfound.NotFoundException;
import de.hsrm.mi.swtp.exchangeplatform.model.data.Model;

import java.util.List;

public interface RestService<T extends Model, Id> {
	
	List<T> getAll();
	
	T getById(Id id) throws NotFoundException;
	
	void save(T item) throws IllegalArgumentException;
	
	void delete(Id id) throws IllegalArgumentException;
	
	boolean update(Id id, T update) throws IllegalArgumentException;
	
}
