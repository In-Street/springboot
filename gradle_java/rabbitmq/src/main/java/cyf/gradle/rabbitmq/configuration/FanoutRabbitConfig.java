package cyf.gradle.rabbitmq.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Cheng Yufei
 * @create 2017-09-26 15:03

Fanout Exchange形式又叫广播形式,因此我们发送到路由器的消息会使得绑定到该路由器的每一个Queue接收到消息,
这个时候就算指定了Key,或者规则(即上文中convertAndSend方法的参数2),也会被忽略!



 **/
@Configuration
public class FanoutRabbitConfig {

    @Bean
    public Queue Amsg() {
        return new Queue("Amsg");
    }

    @Bean
    public Queue Bmsg() {
        return new Queue("Bmsg");
    }

    @Bean
    public Queue Cmsg() {
        return new Queue("Cmsg");
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("fanoutExchange");
    }

    @Bean
    Binding bindingExchangeA(Queue Amsg, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(Amsg).to(fanoutExchange);
    }

    @Bean
    Binding bindingExchangeB(Queue Bmsg, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(Bmsg).to(fanoutExchange);
    }

    @Bean
    Binding bindingExchangeC(Queue Cmsg, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(Cmsg).to(fanoutExchange);
    }
}
