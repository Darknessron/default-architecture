/**
 * 
 */
package ron.architecture.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

import ron.architecture.UserServiceApplication;

/**
 * @author Ron.Tseng
 *
 * @date 2020-06-14 01:11:30
 */
@RestController
public class UserController {
	
	@Autowired
	private RabbitTemplate rabbitTemplate;

	@RequestMapping(path = "/registerAccount", method = RequestMethod.POST)
	public JsonNode createUser(@RequestBody JsonNode user)	{
		JsonNode result = null;
		result = (JsonNode)rabbitTemplate.convertSendAndReceive(UserServiceApplication.writeTopicExchangeName, UserServiceApplication.writeQueueName, user);
		return result;
	}
}
