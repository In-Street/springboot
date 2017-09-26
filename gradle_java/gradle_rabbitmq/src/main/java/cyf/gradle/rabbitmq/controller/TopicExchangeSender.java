package cyf.gradle.rabbitmq.controller;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 发送者
 *  发送者和接收者的queue name必须一致，不然不能接收
 * @author Cheng Yufei
 * @create 2017-09-18 16:46
 **/
@Component
public class TopicExchangeSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send() {

        String context = "exchange_Sender_1 " + "chengyufei";

        System.out.println("exchange_Sender_1 : " + context);

        this.rabbitTemplate.convertAndSend("exchange","topic.message",context);

    }

    public void send2() {

        String context = "exchange_Sender_2 " + "taylor swift";

        System.out.println("exchange_Sender_2 : " + context);

        this.rabbitTemplate.convertAndSend("exchange","topic.messages",context);

    }

}
