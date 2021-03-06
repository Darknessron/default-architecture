
package ron.architecture.apigateway.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ron.architecture.apigateway.utils.TokenUtil;

/**
 * @author Ron.Tseng
 *
 * @date 2020-06-12 23:28:28
 */
@Component
public class PostAuthFilter implements GlobalFilter, Ordered {

	private static final Logger logger = LoggerFactory.getLogger(PostAuthFilter.class);

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		logger.debug("into post");
		ServerHttpResponse originalResponse = exchange.getResponse();
		DataBufferFactory bufferFactory = originalResponse.bufferFactory();
		ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
			@Override
			public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
				// Only modify JSON response and status 200
				if (!originalResponse.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON) || !HttpStatus.OK.equals(originalResponse.getStatusCode())) {
					return chain.filter(exchange);
				}
				if (body instanceof Flux) {
					Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
					return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
						ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
						dataBuffers.forEach(buffer -> {
							byte[] array = new byte[buffer.readableByteCount()];
							buffer.read(array);
							try {
								outputStream.write(array);
							} catch (IOException e) {
								logger.error("Error occur while reading reponse", e);
							}
							DataBufferUtils.release(buffer);
						});
						String responseJson = new String(outputStream.toByteArray());
						logger.debug("Response json string: {}", responseJson);
						JsonNode jsonNode = null;
						ObjectNode node = null;
						try {
							jsonNode = new ObjectMapper().readTree(responseJson);
						}
						catch (Exception e) {
							logger.error("Error while parsing response to JsonNode", e);
						}
							String token = exchange.getRequest().getHeaders().getFirst(PreAuthFilter.TOKEN);
						try	{	
							if (StringUtils.isEmpty(token)) {								
								token = TokenUtil.generateJwt(jsonNode.get("user"));
							}else	{
								token = TokenUtil.renewToken(token);
							}
							node = jsonNode.deepCopy(); 
							node.put("token", token);
						}catch(Exception e)	{
							logger.error("Error while deal with Token", e);
						}

						byte[] write = node.toString().getBytes();
						originalResponse.getHeaders().setContentLength(write.length);
						return bufferFactory.wrap(write);
					}));
				}
				// if body is not a flux. never got there.
				return super.writeWith(body);
			}
		};
		// replace response with decorator
		return chain.filter(exchange.mutate().response(decoratedResponse).build());

	}

	@Override
	public int getOrder() {
		return -2;
	}
}