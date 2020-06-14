/**
 * 
 */
package ron.architecture.apigateway.utils;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import ron.architecture.apigateway.holder.WhitelistHolder;

/**
 * @author Ron.Tseng
 *
 * @date 2020-06-14 00:16:57
 */
@Component
public class RequestUtil {

	private static final String EUREKA_SERVICE = "Eureka-Service";
	
	
	private static WhitelistHolder whitelist;
	
	@Autowired
	private WhitelistHolder holder;
	
	@PostConstruct
    public void init() {
		RequestUtil.whitelist = holder;
    }



	/**
	 * Check the request URI and Method is in whitelist or not
	 * @param request
	 * @return
	 */
	public static boolean isRequestInWhitelist(ServerHttpRequest request) {
		//Treat all services registered in Eureka as in whitelist
		if (request.getHeaders().containsKey(EUREKA_SERVICE)) return true;
		
		String uri = request.getURI().getPath();
		String method = request.getMethodValue();

		if (StringUtils.isEmpty(uri) || StringUtils.isEmpty(method))
			return false;
		return whitelist.getFunctions().stream().parallel()
				.anyMatch(function -> (function.getUri().contains(uri) && method.equals(function.getMethod())));

	}
}
