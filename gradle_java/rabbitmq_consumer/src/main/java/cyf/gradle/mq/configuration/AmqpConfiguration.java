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

import java.util.HashMap;
import java.util.Map;

/**
 * @author Cheng Yufei
 * @create 2017-12-31 下午12:27
 * <p>
 * arguments:
 * x-max-length : queue 消息条数限制
 * x-max-length-bytes ：queue 消息容量限制
 * x-message-ttl： 消息存活时间
 * x-dead-letter-exchange：死信
 * x-dead-letter-routing-key：死信
 * x-expires：queue：存活时间
 * x-max-priority：消息队列优先级
 **/
@Configuration
public class AmqpConfiguration {

    public AmqpConfiguration(AmqpAdmin amqpAdmin) {

        //交换机
        Exchange commonExchange = ExchangeBuilder.directExchange(Constants.COMMON_EXCHANGE).durable(true).build();

        //队列
        Queue commonQueue = new Queue(Constants.COMMON_QUEUE);

        //绑定 - 使用 BindingBuilder 也是根据 new Binding() 进行创建
        Binding commonBind = BindingBuilder.bind(commonQueue).to(commonExchange).with(Constants.COMMON_ROUTING_KEY).noargs();

        // 普通交换机、普通队列、普通routing-key 的声明绑定
        amqpAdmin.declareExchange(commonExchange);
        amqpAdmin.declareQueue(commonQueue);
        amqpAdmin.declareBinding(commonBind);

        /**
         * mq 并没有延时设定，可通过死信实现。
         *
         * 将消息发入死信队列（通过额外参数设定存活时间、时间到了后发入进行消费的交换机和队列），在消息存活时间内不进行消费（延时过程）。
         *
         * 时间到了之后，由死信队列 额外参数map 指定routing-key 对应得队列进行消费。
         *
         */

        //死信交换机
        Exchange deadLetterExchange = ExchangeBuilder.directExchange(Constants.DEAD_LETTER_EXCHANGE).durable(true).build();

        //死信队列参数设置
        Map<String, Object> map = Maps.newHashMap();
        //消息存活时间 60s
        map.put("x-message-ttl", 60000);
        //存活时间过后，指定发入消费的交换机和 延时消费队列的routing-key
        map.put("x-dead-letter-exchange", Constants.DEAD_LETTER_EXCHANGE);
        map.put("x-dead-letter-routing-key", Constants.DELAY_ROUTING_KEY);
        //死信队列
        Queue deadLetterQueue = new Queue(Constants.DEAD_LETTER_QUEUE, true, false, false, map);
        //死信队列 绑定死信交换机及key
        Binding deadLetterBind = BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange).with(Constants.DEAD_LETTER_ROUTING_KEY).noargs();

        amqpAdmin.declareExchange(deadLetterExchange);
        amqpAdmin.declareQueue(deadLetterQueue);
        amqpAdmin.declareBinding(deadLetterBind);


        //延时队列处理死信队列中到期的消息
        Queue delayQueue = new Queue(Constants.DELAY_QUEUE);

        Binding delayBind = BindingBuilder.bind(delayQueue).to(deadLetterExchange).with(Constants.DELAY_ROUTING_KEY).noargs();

        amqpAdmin.declareQueue(delayQueue);
        amqpAdmin.declareBinding(delayBind);


        /**
         * generalQueue 设置死信相关，当存在异常时，消息经过重试进入死信队列
         */
        Map<String, Object> generalMap = new HashMap<>(2);
        generalMap.put("x-dead-letter-exchange", Constants.DEAD_LETTER_EXCHANGE);
        generalMap.put("x-dead-letter-routing-key", Constants.DEAD_LETTER_ROUTING_KEY);

        Queue generalQueue = new Queue(Constants.GENERAL_QUEUE, true, false, false, generalMap);
        Binding generalBind = BindingBuilder.bind(generalQueue).to(commonExchange).with(Constants.GENERAL_ROUTING_KEY).noargs();

        amqpAdmin.declareQueue(generalQueue);
        amqpAdmin.declareBinding(generalBind);

        /**
         * 确保消息不丢失：【消息确认机制、消息持久化机制】
         *
         *    A:  默认情况下，mq投递消息给消费者后就会标记为删除
         *          若消费者挂掉后，投递给消费者的正在执行或未执行的消息丢失。解决方案：mq支持消息确认机制确保消息不丢失：
         *
         *                1.配置文件设为：acknowledge-mode: manual 手动确认模式
         *                2. 消费者执行完后：channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
         *
         *               3.确认后，消费者通知mq ,消息已经接受、处理，rabbitmq 可以删除它了。若consumer挂掉，会将消息重新发送给另一个消费者。
         *
         *   B: rabbitmq server挂掉后，确保消息不丢失机制是 将消息持久化。
         *
         *          1.queue 设置为durable（new Queue 时默认是true）
         *          2.投递messages设置持久化 (convertAndSend时设置MessagePostProcessor的投递模式：默认持久)，确保在rabbitmq 服务挂掉后消息不会丢失
         *
         */
    }
}
