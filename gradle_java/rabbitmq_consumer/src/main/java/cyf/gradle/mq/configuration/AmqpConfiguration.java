package cyf.gradle.mq.configuration;

import com.google.common.collect.Maps;
import cyf.gradle.base.Constants;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author Cheng Yufei
 * @create 2017-12-31 下午12:27
 *
 * arguments:
 *   x-max-length : queue 消息条数限制
 *   x-max-length-bytes ：queue 消息容量限制
 *   x-message-ttl： 消息存活时间
 *   x-dead-letter-exchange：死信
 *   x-dead-letter-routing-key：死信
 *   x-expires：queue：存活时间
 *   x-max-priority：消息队列优先级
 *
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

        //死信队列参数设置
        Map<String, Object> map = Maps.newHashMap();
        map.put("x-message-ttl", 60000);
        map.put("x-dead-letter-exchange", Constants.AMQP_EXCHANGE_DEAD_LETTER);
        map.put("x-dead-letter-routing-key", Constants.AMQP_ROUTING_KEY_DEAD_LETTER);
        //死信队列
        Queue deadLetterQueue = new Queue(Constants.AMQP_QUEUE_DEAD_LETTER,true,false,false,map);
        //死信队列 绑定其他正常交换机及routing key
        Binding deadLetterQueueBind = BindingBuilder.bind(deadLetterQueue).to(msgExchange).with(Constants.AMQP_ROUTING_KEY_MSG).noargs();


        amqpAdmin.declareExchange(msgExchange);
        amqpAdmin.declareQueue(msgQueue);
        amqpAdmin.declareBinding(msgBinding);
        /*amqpAdmin.declareQueue(deadLetterQueue);
        amqpAdmin.declareBinding(deadLetterQueueBind);*/

        //死信交换机
        Exchange deadLetterExchange = ExchangeBuilder.directExchange(Constants.AMQP_EXCHANGE_DEAD_LETTER).durable(true).build();

        //延时队列处理死信
        Queue delayQueue = new Queue(Constants.AMQP_QUEUE_DELAY);

        Binding delayQueueBind = BindingBuilder.bind(delayQueue).to(deadLetterExchange).with(Constants.AMQP_ROUTING_KEY_DEAD_LETTER).noargs();



        amqpAdmin.declareExchange(deadLetterExchange);
        amqpAdmin.declareQueue(delayQueue);
        amqpAdmin.declareBinding(delayQueueBind);
    }
}
