/**
 * 
 */
package ron.architecture.apigateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;
import ron.architecture.apigateway.holder.IgnoreCacheHolder;

/**
 * @author Ron.Tseng
 *
 * @date 2020-05-29 01:40:48
 */
@Component
public class EnableCacheRequestBodyFilter implements GlobalFilter, Ordered {

	@Autowired
	private IgnoreCacheHolder ignoreList;

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		boolean isIgnore = isIgnore(exchange.getRequest());
		if (isIgnore) return chain.filter(exchange);
		return ServerWebExchangeUtils.cacheRequestBody(exchange, (serverHttpRequest) -> {
			// don't mutate and build if same request object
			if (serverHttpRequest == exchange.getRequest()) {
				return chain.filter(exchange);
			}
			return chain.filter(exchange.mutate().request(serverHttpRequest).build());
		});
	}


	/**
	 * Check the request URI and Method is in whitelist or not
	 * @param request
	 * @return
	 */
	private boolean isIgnore(ServerHttpRequest request) {		
		String uri = request.getURI().getPath();
		String method = request.getMethodValue();

		if (StringUtils.isEmpty(uri) || StringUtils.isEmpty(method))
			return false;
		return ignoreList.getFunctions().stream().parallel()
				.anyMatch(function -> (uri.equals(function.getUri()) && method.equals(function.getMethod())));

	}

}
