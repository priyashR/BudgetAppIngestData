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


import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;



@SpringBootApplication
@EnableSwagger2
@ComponentScan({"com.gmail.ramawthar.priyash.rabbit"})
@ComponentScan({"com.gmail.ramawthar.priyash"})
public class DataIngestionApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataIngestionApplication.class, args);


		/*
		FetchEmails fetch = new FetchEmails();
		try{
			fetch.run();
		}catch (Exception e){
			System.out.println(e.getStackTrace());
		}*/
	}
}
