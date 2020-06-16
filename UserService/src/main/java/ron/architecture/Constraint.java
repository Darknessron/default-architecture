/**
 * 
 */
package ron.architecture;

/**
 * @author Ron.Tseng
 *
 * @date 2020-06-15 21:59:46
 */
public interface Constraint {

	public static final String READ_TOPICEXCHANGE_NAME = "read-exchange";
	public static final String WRITE_TOPICEXCHANG_ENAME = "write-exchange";

	public static final String READ_QUEUE_NAME = "read-queue";
	public static final String WRITE_QUEUE_NAME = "write-queue";
	
	public static final String CHAIN_ACTION = "chain-action";
	public static final String CHAIN_OBJECT = "chain-object";
	public static final String CHAIN_RESULT = "chain-result";
}
