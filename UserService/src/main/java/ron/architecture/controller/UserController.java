/**
 * 
 */
package ron.architecture.controller;

import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import ron.architecture.entity.User;
import ron.architecture.repository.UserRepository;
import ron.architecture.utils.AESUtil;
import ron.architecture.utils.NodeUtil;

/**
 * @author Ron.Tseng
 *
 * @date 2020-06-14 01:11:30
 */
@RestController
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Value("${aes.key}")
	private String aesKey;
	
	@Autowired
	private UserRepository repository;
	

	@RequestMapping(path = "/login", method = RequestMethod.POST)
	public JsonNode login(@RequestBody JsonNode user) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode result = mapper.createObjectNode();

		String account = NodeUtil.getNodeValue(user, "account", String.class);
		String password = NodeUtil.getNodeValue(user, "password", String.class);
		
		if (StringUtils.isEmpty(account) || StringUtils.isEmpty(password)) 	{
			result.put("isSuccess", false);
			result.put("error", "Account or Password can not be empty");
			return result;
		}
		
		try {
			//encrypt password
			password = AESUtil.encrypt(password, aesKey);
		} catch (Exception e) {
			logger.error("Error occur while encrypt the password", e);
			result.put("isSuccess", false);
			result.put("error", e.getMessage());
			return result;
		}
		// Fetch User
		Optional<User> response = repository.findByAccountAndPassword(account, password);		
		
		if (response.isPresent())	{
			result.put("isSuccess", true);
			result.set("user", convertEntityTojsonNode(response.get()));
		}else	{
			result.put("isSuccess", false);
			result.put("error", "login fail");
		}
		return result;
	}
	
	private JsonNode convertEntityTojsonNode(User user)	{
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode result = mapper.createObjectNode();
		ObjectNode role = mapper.createObjectNode();
		
		result.put("account", user.getAccount());
		result.put("name", user.getName());
		result.put("email", user.getEmail());
		role.put("roleId", user.getRole().getId());
		role.put("roleName", user.getRole().getName());
		
		result.set("role", role);
		return result;
	}
}
