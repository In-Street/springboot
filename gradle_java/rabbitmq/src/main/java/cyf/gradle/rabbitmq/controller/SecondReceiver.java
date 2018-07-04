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
@RabbitListener(queues = "secondqueue")
public class SecondReceiver {


    @RabbitHandler
    public void process(User user) {
        System.out.println("second Receiver  : " + user);
    }



}
