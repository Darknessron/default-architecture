/**
 * 
 */
package ron.architecture.apigateway.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import reactor.core.publisher.Flux;
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

	public static final String ACCOUNT = "User-Account";
	public static final String TOKEN = "token";

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

		logger.debug("into pre");
		ServerHttpRequest request = exchange.getRequest();
		boolean isWhitelist = RequestUtil.isRequestInWhitelist(request);
		if (isWhitelist)
			return chain.filter(exchange);

		DataBuffer cachedRequestBody = exchange.getAttribute(ServerWebExchangeUtils.CACHED_REQUEST_BODY_ATTR);

		DataBufferFactory factory = exchange.getResponse().bufferFactory();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		InputStream input = cachedRequestBody.asInputStream();

		// Fake code simulating the copy
		// You can generally do better with nio if you need...
		// And please, unlike me, do something about the Exceptions :D
		try {
			byte[] buffer = new byte[1024];
			int len;
			while ((len = input.read(buffer)) > -1) {
				baos.write(buffer, 0, len);
			}
			baos.flush();
		} catch (IOException e) {
			logger.error("Error occur while copy request body", e);
		}

		DataBuffer newDataBuffer = factory.wrap(baos.toByteArray());
		DataBuffer jsonBuffer = factory.wrap(baos.toByteArray());

		JsonNode requestNode = null;
		String token = null;
		try {

			ObjectMapper objectMapper = new ObjectMapper();
			requestNode = objectMapper.readTree(jsonBuffer.asInputStream());
			token = requestNode.get("token").asText();
			logger.debug("Request body {}", requestNode.toPrettyString());
		} catch (IOException e) {
			logger.error("Read request body error", e);
		}
		boolean isValid = false;
		try {
			isValid = TokenUtil.isTokenVaild(token);
			if (!isValid) {
				ServerHttpResponse response = exchange.getResponse();
				response.setStatusCode(HttpStatus.FORBIDDEN);
				writeResponseMessage(response, "No permission to perform this operation");
			}
		} catch (ExpiredJwtException e) {
			ServerHttpResponse response = exchange.getResponse();
			response.setStatusCode(HttpStatus.FORBIDDEN);
			writeResponseMessage(response, "Token Expired !!");
		}
		ServerHttpRequest decoratorRequest = decorator(request, newDataBuffer).mutate()
				.header(ACCOUNT, TokenUtil.getAccount(token)).build();
		decoratorRequest = decoratorRequest.mutate().header(TOKEN, token).build();

		return chain.filter(exchange.mutate().request(decoratorRequest).build());
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE + 1001;
	}

	private Mono<Void> writeResponseMessage(ServerHttpResponse response, String message) {
		DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
		DataBuffer data = dataBufferFactory.wrap(message.getBytes());
		return response.writeWith(Mono.just(data));
	}

	private ServerHttpRequest decorator(ServerHttpRequest request, DataBuffer newDataBuffer) {
		return new ServerHttpRequestDecorator(request) {
			@Override
			public Flux<DataBuffer> getBody() {
				return Flux.just(newDataBuffer);
			}
		};
	}

}
