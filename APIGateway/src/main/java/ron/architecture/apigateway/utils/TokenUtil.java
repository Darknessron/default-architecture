/**
 * 
 */
package ron.architecture.apigateway.utils;

import java.security.Key;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
 * @author Ron.Tseng
 *
 * @date 2020-06-14 16:39:16
 */
@Component
public class TokenUtil {

	private static final Logger logger = LoggerFactory.getLogger(TokenUtil.class);
	static final String ISSUER = "Ron.Tseng";

	private static String secret;	

	@Value("${jwt.secret}")
	private String secretKey;
	
	@PostConstruct
    public void init() {
		TokenUtil.secret = secretKey;
    }
	
	/**
	 * Check the given token is valid or not.
	 * @param token
	 * @return
	 */
	public static boolean isTokenVaild(String token)	{
		if (StringUtils.isEmpty(token)) return false;
		
		Claims claims = extractJwt(token);
		if (claims == null) return false;
		Date now = new Date();
		return ISSUER.equals(claims.getIssuer()) && now.before(claims.getExpiration());
	}
	
	/**
	 * Renew the expiration of given token
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public static String renewToken(String token) throws Exception{
		if (StringUtils.isEmpty(token)) {
			throw new Exception("the token is empty.");
		}
		Claims claims = extractJwt(token);
		if (claims == null)	{
			throw new Exception("Parsing token error!!!");
		}else	{
			claims.setExpiration(new Date(System.currentTimeMillis() + 3600000));
			return Jwts.builder().setClaims(claims).compact();
		}
	}
	
	/**
	 * Extract token with secret key
	 * 
	 * @param token
	 * @return
	 */
	private static Claims extractJwt(String token)	{
		Claims claims = null;
		try	{
			claims = (Claims)Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parse(token).getBody();
		}catch(Exception e)	{
			logger.error("Parsing token error!!!", e);
			throw e;
		}
		return claims;
		
	}
	
	/**
	 * Generate Jwt with Secret Key and expire time
	 * 
	 * @param userResult
	 * @return
	 */
	public static String generateJwt(JsonNode user) {
		String result = null;
		String role = user.get("role").asText();
		String account = user.get("account").asText();
		Key key = getSecretKey();
		// Expire time : 60 mins
		result = Jwts.builder().setIssuer(ISSUER).setSubject("Role")
				.claim("role", role)
				.claim("account", account)
				.setExpiration(new Date(System.currentTimeMillis() + 3600000)).signWith(key).compact();
		return result;
	}
	
	/**
	 * Create the Key with secret string. 
	 * @return
	 */
	private static Key getSecretKey()	{
		Key key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
		return key;
	}
}
