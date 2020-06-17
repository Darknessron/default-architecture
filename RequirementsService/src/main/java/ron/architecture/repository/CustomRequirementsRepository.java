/**
 * 
 */
package ron.architecture.repository;

import java.util.Date;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author Ron.Tseng
 *
 * @date 2020-06-17 21:00:37
 */
public interface CustomRequirementsRepository {
	public JsonNode findRequirement(@Nullable String category, @Nullable String serialNumber, @Nullable String subject, @Nullable String requester, @Nullable Date requestStartTime, @Nullable Date requestEndstartTime, @Nullable Integer pageSize, @Nullable Integer pageNumber);
}
