package cyf.gradle.rabbitmq.controller;

import cyf.gradle.rabbitmq.modal.User;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 发送者
 *  发送者和接收者的queue name必须一致，不然不能接收
 * @author Cheng Yufei
 * @create 2017-09-18 16:46
 **/
@Component
public class FirstSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send() {

        String context = "Sender " + "chengyufei";

        System.out.println("Sender : " + context);

        this.rabbitTemplate.convertAndSend("firstqueue", context);

    }

}
