package com.gmail.ramawthar.priyash.DataIngestion;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.gmail.ramawthar.priyash.rabbit.Receiver;



@SpringBootApplication
@ComponentScan({"com.gmail.ramawthar.priyash.rabbit"})
public class DataIngestionApplication { 
	/* moved to the configuration class for rabbit
    static final String topicExchangeName = "budget-exchange";
    static final String queueName = "fnb-transactions";
    static final String routingKey = "fnb.trxn.#";

    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }*/
/* Moved the queue listener out of this class
    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }
*/
    
    /*testing autowiring <- test did not work -> wont work because component classes cannot be created with the new keyword
    
	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory("172.18.0.2");
		connectionFactory.setPort(5672);
		connectionFactory.setUsername("budget");
		connectionFactory.setPassword("fnbTrxn");
		return connectionFactory;
	}
	
	@Bean
	public RabbitTemplate rabbitTemplate() {
		RabbitTemplate template = new RabbitTemplate(connectionFactory());
		//The routing key is set to the name of the exchange.
		template.setExchange(topicExchangeName);
		template.setRoutingKey(routingKey);
		//Where we will synchronously receive messages from
		//template.setQueue(this.helloWorldQueueName);
		return template;
	}*/


	public static void main(String[] args) {
		SpringApplication.run(DataIngestionApplication.class, args);
		
		FetchEmails fetch = new FetchEmails();
		try{
			fetch.run();
		}catch (Exception e){
			System.out.println(e.getStackTrace());
		}
	}
}
