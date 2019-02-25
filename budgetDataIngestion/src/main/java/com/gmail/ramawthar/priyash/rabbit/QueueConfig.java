package com.gmail.ramawthar.priyash.rabbit;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfig {


    static final String topicExchangeName = "budget-exchange";
    static final String queueName = "fnb-transactions-pers";
    static final String routingKey = "fnb.trxn.#";
    //static final String IP = "127.0.0.53";
    static final String IP = "172.16.0.128";//do an ipconfig to get the host ip
    static final int port = 5672;

    @Bean
    Queue queue() {
        return new Queue(queueName, true);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }
    
	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(IP);
		connectionFactory.setPort(port);
		connectionFactory.setUsername("budget");
		connectionFactory.setPassword("fnbTrxn");
		return connectionFactory;
	}
	
	@Bean
	public AmqpAdmin amqpAdmin() {
		return new RabbitAdmin(connectionFactory());
	}

	@Bean
	public RabbitTemplate rabbitTemplate() {
		RabbitTemplate template = new RabbitTemplate(connectionFactory());
		//The routing key is set to the name of the queue by the broker for the default exchange.
		//template.setRoutingKey(this.helloWorldQueueName);
		template.setExchange(topicExchangeName);
		template.setRoutingKey(routingKey);
		//Where we will synchronously receive messages from
		//template.setQueue(this.helloWorldQueueName);
		//System.out.println("Register the rabbitTemplate");
		return template;
	}
}
