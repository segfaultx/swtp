package de.hsrm.mi.swtp.exchangeplatform.service.authentication;

import de.hsrm.mi.swtp.exchangeplatform.model.authentication.ActiveTokens;
import de.hsrm.mi.swtp.exchangeplatform.model.data.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JWTTokenUtils {
	
	public final static String BEARER_TOKEN_PREFIX = "Bearer ";
	public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

	private ActiveTokens activeTokens;
	
	@Value("${jwt.secret}")
	private String secret;
	
	/**
	 * Retrieves the Username of the given token
	 * @param token JWT
	 * @return Username
	 */
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}
	
	/**
	 * Retrieves expiration Date of given token
	 * @param token JWT
	 * @return Expiration date
	 */
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}
	
	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}
	
	/**
	 * Gets all claims in body of token, by using the secret key defined
	 * @param token JWT
	 * @return all claims in token
	 */
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}
	
	/**
	 * Checks if token is expired
	 * @param token JWT
	 * @return true if expired, false if not
	 */
	public Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}
	
	/**
	 * Generates JWT for user
	 * @param user user that is authenticated and needs a token
	 * @return JWT
	 */
	public String generateToken(User user) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, user.getAuthenticationInformation().getUsername());
	}
	
	/**
	 * Creating method of JWT
	 *
	 * Defines claims for the token
	 * Sets creation and expiration date
	 * Signs token with HS312 Hashing Algorithm
	 * Compacts the token to a URL-safe string
	 * @param claims claims for JWT
	 * @param subject Username for JWT
	 * @return JWT
	 */
	private String doGenerateToken(Map<String, Object> claims, String subject) {
		return Jwts.builder()
				   .setClaims(claims)
				   .setSubject(subject)
				   .setIssuedAt(new Date(System.currentTimeMillis()))
				   .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
				   .signWith(SignatureAlgorithm.HS512, secret)
				   .compact();
	}
	
	/**
	 * Validates token with authenticated User, so that one does not simply change the claims
	 * @param token JWT
	 * @param userDetails authenticated UserDetails
	 * @return true if valid, false if not
	 */
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername())
				&& !isTokenExpired(token)
				&& activeTokens.getActiveTokens().contains(token));
	}
	
	/**
	 * Gets JWT without prefix
	 * @param bearerToken Token with bearer token prefix in front
	 * @return Substring without bearer token prefix
	 */
	public static String tokenWithoutPrefix(final String bearerToken) {
		return bearerToken.replace(BEARER_TOKEN_PREFIX, "");
	}
	
	/**
	 * checks if JWT is not null and starts with bearer token prefix
	 * @param token Token from header
	 * @return true if valid, false if invalid
	 */
	public static boolean isValidToken(final String token) {
		return token != null && token.startsWith(BEARER_TOKEN_PREFIX);
	}
	
	//TODO: check if needed, not used
	public ActiveTokens getActiveTokens() {
		return activeTokens;
	}
	
	@Autowired
	public void setActiveTokens(ActiveTokens activeTokens) {
		this.activeTokens = activeTokens;
	}
}