package cyf.gradle.rabbitmq.controller;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author Cheng Yufei
 * @create 2017-09-26 15:19
 **/
@Component
public class FanoutReceiver {

    @RabbitListener(queues = "Amsg")
    public void process_1(String parameter) {
        System.out.println("Amsg :" + parameter);

    }

    @RabbitListener(queues = "Bmsg")
    public void process_2(String parameter) {
        System.out.println("Bmsg :" + parameter);

    }

    @RabbitListener(queues = "Cmsg")
    public void process_3(String parameter) {
        System.out.println("Cmsg :" + parameter);

    }
}

