package cyf.gradle.rabbitmq.configuration;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 队列配置
 * @author Cheng Yufei
 * @create 2017-09-18 16:40
 *
Topic Exchange 转发消息主要是根据通配符。 在这种交换机下，队列和交换机的绑定会定义一种路由模式，
那么，通配符就要在这种路由模式和路由键之间匹配后交换机才能转发消息。
在这种交换机模式下：
路由键必须是一串字符，用句号（.） 隔开，比如说 agreements.us，或者 agreements.eu.stockholm 等。
路由模式必须包含一个 星号（*），主要用于匹配路由键指定位置的一个单词，比如说，一个路由模式是这样子：agreements..b.*，

那么就只能匹配路由键是这样子的：
第一个单词是 agreements，第四个单词是 b。
井号（#）就表示相当于一个或者多个单词，例如一个匹配模式是agreements.eu.berlin.#，那么，以agreements.eu.berlin开头的路由键都是可以的。

topic 和 direct 类似, 只是匹配上支持了”模式”, 在”点分”的 routing_key 形式中, 可以使用两个通配符:

 *表示一个词.
#表示零个或多个词.

 **/
@Configuration
public class TopicRabbitConfig {

    final static String message = "topic.message";
    final static String messages = "topic.messages";

    @Bean
    public Queue queueMessage() {
        return new Queue(TopicRabbitConfig.message);
    }

    @Bean
    public Queue queueMessages() {
        return new Queue(TopicRabbitConfig.messages);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange("exchange");
    }

    //参数 queue 名称 需要和上面定义的Queue 的名称一致
    @Bean
    Binding bindingExchangeMessage(Queue queueMessage, TopicExchange exchange) {
        return BindingBuilder.bind(queueMessage).to(exchange).with("topic.message");
    }

    //参数 queue 名称 需要和上面定义的Queue 的名称一致
    @Bean
    Binding bindingExchangeMessages(Queue queueMessages, TopicExchange exchange) {
        System.out.println();
        return BindingBuilder.bind(queueMessages).to(exchange).with("topic.#");
    }
}
