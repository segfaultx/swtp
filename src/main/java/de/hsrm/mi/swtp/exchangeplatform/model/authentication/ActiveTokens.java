package de.hsrm.mi.swtp.exchangeplatform.model.authentication;

import de.hsrm.mi.swtp.exchangeplatform.service.authentication.JWTTokenUtils;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
public class ActiveTokens {
	
	JWTTokenUtils tokenUtils;

	List<String> activeTokens;
	
	public ActiveTokens() {
		this.activeTokens = new ArrayList<>();
	}
	
	/**
	 * Adds given token to the List of active Tokens, after calling removeAllExpiredTokens
	 * @param token the Token to be added to the list
	 * @return updated List with all active tokens
	 */
	public List<String> addToken(String token) {
		removeAllExpiredTokens();
		
		this.activeTokens.add(token);
		return this.activeTokens;
	}
	
	/**
	 * Removes given token if present in List, after calling removeAllExpiredTokens
	 * @param token the Token to be removed from the List
	 * @return updated List with all active tokens
	 */
	public List<String> removeToken(String token) {
		
		removeAllExpiredTokens();
		
		for(String elem: this.activeTokens) {
			if(token.equals(elem)) {
				this.activeTokens.remove(token);
			}
		}
		return this.activeTokens;
	}
	
	/**
	 * Removes all expired Tokens in the current active Token list
	 */
	private void removeAllExpiredTokens() {
		this.activeTokens.removeIf(elem -> !tokenUtils.isTokenExpired(elem));
	}
	
	@Autowired
	public void setTokenUtils(JWTTokenUtils tokenUtils) {
		this.tokenUtils = tokenUtils;
	}
}
