/**
 * 
 */
package ron.architecture.apigateway.holder;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Ron.Tseng
 *
 * @date 2020-05-15 01:39:04
 */
@Component
@ConfigurationProperties("whitelist")
public class WhitelistHolder {

	private List<FunctionsHolder> functions;

	/**
	 * @return the functions
	 */
	public List<FunctionsHolder> getFunctions() {
		return functions;
	}

	/**
	 * @param functions the functions to set
	 */
	public void setFunctions(List<FunctionsHolder> functions) {
		this.functions = functions;
	}
}