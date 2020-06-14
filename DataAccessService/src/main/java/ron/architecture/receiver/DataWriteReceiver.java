/**
 * 
 */
package ron.architecture.receiver;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author Ron.Tseng
 *
 * @date 2020-06-14 00:49:52
 */
@Component
public class DataWriteReceiver {

	private static final Logger logger = LoggerFactory.getLogger(DataWriteReceiver.class);

	private CountDownLatch latch = new CountDownLatch(1);
	
	public JsonNode receiveMessage(JsonNode message) {
		logger.debug("Receive write message: {}", message);
	    latch.countDown();
	    return message;
	  }

	  public CountDownLatch getLatch() {
	    return latch;
	  }
}
