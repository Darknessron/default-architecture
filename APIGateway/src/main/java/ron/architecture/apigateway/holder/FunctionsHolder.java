/**
 * 
 */
package ron.architecture.apigateway.holder;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Ron.Tseng
 *
 * @date 2020-05-15 01:39:37
 */
@ConfigurationProperties("whitelist.functions")
public class FunctionsHolder {

	private String uri;
	private String method;

	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * @param uri the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}
}
