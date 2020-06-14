/**
 * 
 */
package ron.architecture.apigateway.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;
import ron.architecture.apigateway.utils.RequestUtil;
import ron.architecture.apigateway.utils.TokenUtil;

/**
 * @author Ron.Tseng
 *
 * @date 2020-05-14 17:21:48
 */
@Component
public class PreAuthFilter implements GlobalFilter, Ordered {

	private static final Logger logger = LoggerFactory.getLogger(PreAuthFilter.class);

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		boolean isWhitelist = RequestUtil.isRequestInWhitelist(request);
		if (isWhitelist)
			return chain.filter(exchange);

		DataBuffer cachedRequest = exchange.getAttribute(ServerWebExchangeUtils.CACHED_REQUEST_BODY_ATTR);
		JsonNode requestNode = null;
		String token = null;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			requestNode = objectMapper.readTree(cachedRequest.asInputStream());
			token = requestNode.get("token").asText();
			logger.debug("Request body {}", requestNode.toPrettyString());
		} catch (IOException e) {
			logger.error("Read request body error", e);
		}
		boolean isValid = TokenUtil.isTokenVaild(token);
		if (!isValid) {
			ServerHttpResponse response = exchange.getResponse();
			response.setStatusCode(HttpStatus.FORBIDDEN);
			writeResponseMessage(response, "No permission to perform this operation");
		}

		return chain.filter(exchange);
	}

	@Override
	public int getOrder() {
		return 1;
	}

	private Mono<Void> writeResponseMessage(ServerHttpResponse response, String message) {
		DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
		DataBuffer data = dataBufferFactory.wrap(message.getBytes());
		return response.writeWith(Mono.just(data));
	}

}
