package cyf.gradle.rabbitmq.controller;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Cheng Yufei
 * @create 2017-09-26 15:20
 **/
@Component
public class FanoutSender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void send() {
        String context = "fanoutSender : " + "ChengYufei";
        amqpTemplate.convertAndSend("fanoutExchange","",context);

    }
}
