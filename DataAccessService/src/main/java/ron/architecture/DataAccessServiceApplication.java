package ron.architecture;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import ron.architecture.receiver.DataReadReceiver;
import ron.architecture.receiver.DataWriteReceiver;

@SpringBootApplication
public class DataAccessServiceApplication {

	public static final String readTopicExchangeName = "read-exchange";
	public static final String writeTopicExchangeName = "write-exchange";

	public static final String readQueueName = "read-queue";
	public static final String writeQueueName = "write-queue";

	public static void main(String[] args) {
		SpringApplication.run(DataAccessServiceApplication.class, args);
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

	@Bean
	SimpleMessageListenerContainer readContainer(ConnectionFactory connectionFactory,
			@Autowired MessageListenerAdapter readListenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(readQueueName);
		container.setMessageListener(readListenerAdapter);
		return container;
	}

	@Bean
	SimpleMessageListenerContainer writeContainer(ConnectionFactory connectionFactory,
			@Autowired MessageListenerAdapter writeListenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(writeQueueName);
		container.setMessageListener(writeListenerAdapter);
		return container;
	}

	@Bean("readListenerAdapter")
	MessageListenerAdapter readListenerAdapter(DataReadReceiver receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}

	@Bean("writeListenerAdapter")
	MessageListenerAdapter writeListenerAdapter(DataWriteReceiver receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}

	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

}
