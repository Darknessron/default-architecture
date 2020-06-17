/**
 * 
 */
package ron.architecture.utils;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

import ron.architecture.RequirementsServiceApplication;
import ron.architecture.controller.RequirementsController;

/**
 * @author Ron.Tseng
 *
 * @date 2020-06-16 20:27:10
 */
public class NodeUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(NodeUtil.class);

	/**
	 * Get Value from JsonNode with specified type
	 * @param <T>
	 * @param node
	 * @param fieldName
	 * @param resultClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getNodeValue(JsonNode node, String fieldName, Class<T> resultClass )	{
		if (node.has(fieldName)) {
			if (Date.class == resultClass)	{
				try	{
					return (T) RequirementsServiceApplication.sdf.parse(node.get(fieldName).asText());
				}catch(Exception e)	{
					logger.error("Parsing String to Date error", e);
				}
			}
			return resultClass.cast(node.get(fieldName).asText());
		}
		else return null;
	}
}
