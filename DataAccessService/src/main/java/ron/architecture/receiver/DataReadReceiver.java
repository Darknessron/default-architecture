/**
 * 
 */
package ron.architecture.receiver;

import java.util.concurrent.CountDownLatch;

import org.springframework.stereotype.Component;

/**
 * @author Ron.Tseng
 *
 * @date 2020-06-14 00:48:40
 */
@Component
public class DataReadReceiver {

	private CountDownLatch latch = new CountDownLatch(1);
	
	public void receiveMessage(String message) {
	    System.out.println("Received <" + message + ">");
	    latch.countDown();
	  }

	  public CountDownLatch getLatch() {
	    return latch;
	  }
}
