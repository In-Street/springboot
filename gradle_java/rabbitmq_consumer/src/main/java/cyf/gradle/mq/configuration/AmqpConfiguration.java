package cyf.gradle.mq.configuration;

import com.google.common.collect.Maps;
import cyf.gradle.base.Constants;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author Cheng Yufei
 * @create 2017-12-31 下午12:27
 **/
@Configuration
public class AmqpConfiguration {

    public AmqpConfiguration(AmqpAdmin amqpAdmin) {

        //交换机
        Exchange msgExchange = ExchangeBuilder.directExchange(Constants.AMQP_EXCHANGE_MSG).durable(true).build();

        //队列
        Queue msgQueue = new Queue(Constants.AMQP_QUEUE_MSG);

        //绑定 - 使用 BindingBuilder 也是根据 new Binding() 进行创建
        Binding msgBinding = BindingBuilder.bind(msgQueue).to(msgExchange).with(Constants.AMQP_ROUTING_KEY_MSG).noargs();


        Map<String, Object> map = Maps.newHashMap();
        map.put("x-message-ttl", 60000);
        map.put("x-dead-letter-exchange", Constants.AMQP_EXCHANGE_DEAD_LETTER);
        map.put("x-dead-letter-routing-key", Constants.AMQP_ROUTING_KEY_DEAD_LETTER);
        //死信队列
        Queue deadLetterQueue = new Queue(Constants.AMQP_QUEUE_DEAD_LETTER,true,false,false,map);

        Binding deadLetterQueueBind = BindingBuilder.bind(deadLetterQueue).to(msgExchange).with(Constants.AMQP_ROUTING_KEY_MSG).noargs();


        amqpAdmin.declareExchange(msgExchange);
        /*amqpAdmin.declareQueue(msgQueue);
        amqpAdmin.declareBinding(msgBinding);*/
        amqpAdmin.declareQueue(deadLetterQueue);
        amqpAdmin.declareBinding(deadLetterQueueBind);

        //死信交换机
        Exchange deadLetterExchange = ExchangeBuilder.directExchange(Constants.AMQP_EXCHANGE_DEAD_LETTER).durable(true).build();

        //延时队列
        Queue delayQueue = new Queue(Constants.AMQP_QUEUE_DELAY);

        Binding delayQueueBind = BindingBuilder.bind(delayQueue).to(deadLetterExchange).with(Constants.AMQP_ROUTING_KEY_DEAD_LETTER).noargs();



        amqpAdmin.declareExchange(deadLetterExchange);
        amqpAdmin.declareQueue(delayQueue);
        amqpAdmin.declareBinding(delayQueueBind);
    }
}
