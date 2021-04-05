package org.sun.bright.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {

    /**
     * 队列 起名：directQueue
     */
    @Bean
    public Queue directQueue() {
        return new Queue("directQueue",true);
    }

    /**
     * Direct交换机 起名：directExchange
     */
    @Bean
    DirectExchange directExchange() {
        return new DirectExchange("directExchange");
    }

    /**
     * 绑定  将队列和交换机绑定, 并设置用于匹配键：directRouting
     */
    @Bean
    Binding bindingDirect() {
        return BindingBuilder.bind(directQueue())
                .to(directExchange())
                .with("directRouting");
    }

}
