/**
 * 
 */
package ron.architecture.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import ron.architecture.entity.Requirement;
import ron.architecture.repository.RequirementsRepository;
import ron.architecture.utils.NodeUtil;


/**
 * @author Ron.Tseng
 *
 * @date 2020-06-15 04:45:03
 */
@RestController
public class RequirementsController {

	private static final String ACCOUNT = "User-Account";
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	
	private static final Logger logger = LoggerFactory.getLogger(RequirementsController.class);
	
	@Autowired
	private RequirementsRepository repository;
	
	@RequestMapping(path = "/submit", method = RequestMethod.POST)
	public JsonNode submit(@RequestBody JsonNode requirementNode, @RequestHeader(ACCOUNT) String account)	{
		ObjectNode result = new ObjectMapper().createObjectNode();
		logger.debug("Submitter account: {}", account);
		
		String catagory = requirementNode.get("category").asText();
		String serialNumber = generateSerialNumber(catagory);
		Requirement requirement = new Requirement();
		
		requirement.setCategory(catagory);
		requirement.setContent(requirementNode.get("content").asText());
		requirement.setSubject(requirementNode.get("subject").asText());
		requirement.setRequester(account);
		requirement.setSerialNumber(serialNumber);
		requirement.setIsApprove(false);
		requirement.setRequestTime(new Date());
		
		requirement = repository.save(requirement);
		
		
		result.put("isSuccess", true);
		result.put("message", String.format("Subject %1s has benn submitted!", requirement.getSubject()));
		result.put("Serial Number", String.format("Serial Number : %1s", serialNumber));
		
		return result;
	}
	
	@RequestMapping(path = "/findRequirements", method = RequestMethod.POST)
	public JsonNode findRequirement(@RequestBody JsonNode node)	{
		String category = NodeUtil.getNodeValue(node, "category", String.class);
		String serialNumber = NodeUtil.getNodeValue(node, "serialNumber", String.class);
		String subject = NodeUtil.getNodeValue(node, "subject", String.class);
		String requester = NodeUtil.getNodeValue(node, "requester", String.class);
		Date requestStartTime = NodeUtil.getNodeValue(node, "requestStartTime", Date.class);
		Date requestEndstartTime = NodeUtil.getNodeValue(node, "requestEndstartTime", Date.class);
		
		Integer pageSize = null;
		Integer pageNumber = null;
		if (node.get("pagination") != null)	{
			pageSize = NodeUtil.getNodeValue(node.get("pagination"), "pageSize", Integer.class);
			pageNumber = NodeUtil.getNodeValue(node.get("pagination"), "pageNumber", Integer.class);
		}		
		
		return repository.findRequirement(category, serialNumber, subject, requester, requestStartTime, requestEndstartTime, pageSize, pageNumber);
		
		
	}
	
	
	private String generateSerialNumber(String catagory)	{
		return String.format("%1s-%2s", catagory, sdf.format(new Date()));
	}
}
