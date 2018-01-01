package cyf.gradle.mq.service;

import com.rabbitmq.client.Channel;
import cyf.gradle.base.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Cheng Yufei
 * @create 2017-12-31 下午7:58
 **/
@Slf4j
@Component
public class MsgConsumer {

//    @RabbitListener(queues = Constants.AMQP_QUEUE_MSG)
    public void msgConsumer(String json, Channel channel, Message message) {

        System.out.println(json);
//        parallelStream().forEachOrdered
        try {
            // 配置文件中为手动应答，所以这里需 应答
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            try {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,false);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }

    }

    @RabbitListener(queues = Constants.AMQP_QUEUE_DELAY)
    public void delayConsumer(String json, Channel channel, Message message) {

        System.out.println(json);
        try {
            // 配置文件中为手动应答，所以这里需 应答
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            try {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,false);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }

    }

}
