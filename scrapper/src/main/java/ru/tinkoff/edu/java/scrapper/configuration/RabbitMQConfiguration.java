package ru.tinkoff.edu.java.scrapper.configuration;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;

public class RabbitMQConfiguration {
    String exchangeName;
    String queueName;

    public RabbitMQConfiguration(ApplicationConfig config) {
        this.exchangeName = config.exchangeName();
        this.queueName = config.queueName();
    }
    @Bean
    public CachingConnectionFactory connectionFactory(){
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory("localhost");
        cachingConnectionFactory.setUsername("user");
        cachingConnectionFactory.setPassword(("password"));
        return cachingConnectionFactory;
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(exchangeName);
    }
    @Bean
    public RabbitTemplate rabbitTemplate(){
        return new RabbitTemplate(connectionFactory());
    }

    @Bean
    public Queue queue() {
        return new Queue(queueName);
    }

    @Bean
    Binding binding(DirectExchange directExchange, Queue queue) {
        return BindingBuilder.bind(queue()).to(directExchange()).with("directRoutingKey");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}