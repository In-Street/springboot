package cyf.gradle.mq.configuration;

import cyf.gradle.base.Constants;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Cheng Yufei
 * @create 2017-12-31 下午12:27
 **/
@Configuration
public class AmqpConfiguration {

    public AmqpConfiguration (AmqpAdmin amqpAdmin) {

        //交换机
        Exchange msgExchange = ExchangeBuilder.directExchange(Constants.AMQP_EXCHANGE_MSG).durable(true).build();

        //队列
        Queue msgQueue = new Queue(Constants.AMQP_QUEUE_MSG);

        //绑定 - 使用 BindingBuilder 也是根据 new Binding() 进行创建
        Binding msgBinding = BindingBuilder.bind(msgQueue).to(msgExchange).with(Constants.AMQP_ROUTING_KEY_MSG).noargs();


        amqpAdmin.declareExchange(msgExchange);
        amqpAdmin.declareQueue(msgQueue);
        amqpAdmin.declareBinding(msgBinding);



    }
}
