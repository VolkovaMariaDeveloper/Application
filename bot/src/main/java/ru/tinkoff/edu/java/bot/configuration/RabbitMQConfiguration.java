package ru.tinkoff.edu.java.bot.configuration;

import java.util.HashMap;
import java.util.Map;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.ClassMapper;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.bot.dto.LinkUpdateRequest;

@Configuration
public class RabbitMQConfiguration {
    private final String exchangeName;
    private final String queueName;
    private final String DEAD_LETTER_EX = "x-dead-letter-exchange";
    private final String DLQ = ".dlq";
    private final String DLX = ".dlx";

    public RabbitMQConfiguration(ApplicationConfig config) {
        this.exchangeName = config.exchangeName();
        this.queueName = config.queueName();
    }

    @Bean
    public DirectExchange directExchange() {

        return new DirectExchange(exchangeName);
    }

    @Bean
    public DirectExchange deadDirectExchange() {
        return new DirectExchange(exchangeName + DLX);
    }

    @Bean
    public Queue queue() {
        return QueueBuilder.durable(queueName)
            .withArgument(DEAD_LETTER_EX, exchangeName + DLX)
            .build();
    }

    @Bean
    Queue deadQueue() {
        return QueueBuilder.durable(queueName + DLQ).build();
    }

    @Bean
    Binding binding(DirectExchange directExchange, Queue queue) {
        return BindingBuilder.bind(queue()).to(directExchange()).with(queueName);
    }

    @Bean
    public Binding deadBinding() {
        return BindingBuilder.bind(deadQueue()).to(deadDirectExchange()).with(queueName + DLQ);
    }

    @Bean
    public ClassMapper classMapper() {
        Map<String, Class<?>> mappings = new HashMap<>();
        mappings.put("ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest", LinkUpdateRequest.class);

        DefaultClassMapper classMapper = new DefaultClassMapper();
        classMapper.setTrustedPackages("ru.tinkoff.edu.java.scrapper.service.dto.*");
        classMapper.setIdClassMapping(mappings);
        return classMapper;
    }

    @Bean
    public MessageConverter jsonMessageConverter(ClassMapper classMapper) {
        Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter();
        jsonConverter.setClassMapper(classMapper);
        return jsonConverter;
    }
}
