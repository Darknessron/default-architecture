/**
 * 
 */
package ron.architecture.utils;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author Ron.Tseng
 *
 * @date 2020-06-16 20:27:10
 */
public class NodeUtil {

	/**
	 * Get Value from JsonNode with specified type
	 * @param <T>
	 * @param node
	 * @param fieldName
	 * @param resultClass
	 * @return
	 */
	public static <T> T getNodeValue(JsonNode node, String fieldName, Class<T> resultClass )	{
		if (node.has(fieldName)) return resultClass.cast(node.get(fieldName).asText());
		else return null;
	}
}
