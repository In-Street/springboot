package cyf.gradle.rabbitmq.controller;

import cyf.gradle.rabbitmq.modal.User;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * 接受者
 *发送者和接收者的queue name必须一致，不然不能接收
 * @author Cheng Yufei
 * @create 2017-09-18 16:55
 **/
@Component
@RabbitListener(queues = "firstqueue"/*,containerFactory="rabbitListenerContainerFactory"*/)
public class FirstReceiver {

    @RabbitHandler
    public void process(String parameter) {
        System.out.println("Receiver  : " + parameter);
    }



}
