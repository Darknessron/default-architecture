package ron.architecture;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
public class UserServiceApplication {

	public static final String readTopicExchangeName = "read-exchange";
	public static final String writeTopicExchangeName = "write-exchange";

	public static final String readQueueName = "read-queue";
	public static final String writeQueueName = "write-queue";

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	@Bean("readQueue")
	Queue readQueue() {
		return new Queue(readQueueName, false);
	}

	@Bean("writeQueue")
	Queue writeQueue() {
		return new Queue(writeQueueName, false);
	}

	@Bean
	TopicExchange readExchange() {
		return new TopicExchange(readTopicExchangeName);
	}

	@Bean
	TopicExchange writeExchange() {
		return new TopicExchange(writeTopicExchangeName);
	}

	@Bean
	Binding readBinding(@Autowired Queue readQueue, @Autowired TopicExchange readExchange) {
		return BindingBuilder.bind(readQueue()).to(readExchange()).with(readQueue().getName());
	}

	@Bean
	Binding writeBinding(@Autowired Queue writeQueue, @Autowired TopicExchange writeExchange) {
		return BindingBuilder.bind(writeQueue()).to(writeExchange()).with(writeQueue().getName());
	}

}
