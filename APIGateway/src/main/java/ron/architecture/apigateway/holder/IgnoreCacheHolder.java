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
 * @date 2020-05-29 02:11:41
 */
@Component
@ConfigurationProperties("ignorecache")
public class IgnoreCacheHolder {

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
