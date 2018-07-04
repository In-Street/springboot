package cyf.gradle.rabbitmq.controller;

import cyf.gradle.rabbitmq.modal.User;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 接受者
 *发送者和接收者的queue name必须一致，不然不能接收
 * @author Cheng Yufei
 * @create 2017-09-18 16:55
 **/
@Component

public class TopicExchangeReceiver {

    @RabbitListener(queues = "topic.message")
//    @RabbitHandler
    public void process(String context) {
        System.out.println("exchange_Receiver_1  : " + context);
    }


    @RabbitListener(queues = "topic.messages")
//    @RabbitHandler
    public void process2(String context) {
        System.out.println("exchange_Receiver_2  : " + context);
    }

}
