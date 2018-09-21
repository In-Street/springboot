package cyf.gradle.mq.service;

import com.rabbitmq.client.Channel;
import cyf.gradle.base.Constants;
import cyf.gradle.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

/**
 * @author Cheng Yufei
 * @create 2017-12-31 下午7:58
 **/
@Slf4j
@Component
public class MsgConsumer {



    @RabbitListener(queues = Constants.COMMON_QUEUE)
    public void msgConsumer(String json, Channel channel, Message message) {
        String time = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").format(new Date());
        LogUtil.debug(log, "正常消息处理：时间：{} 消息体：{} threadlocal：{}", time, json, Thread.currentThread().getName());
        System.out.println(json);
//        parallelStream().forEachOrdered
        try {
            // 配置文件中为手动应答，所以这里需 应答
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            try {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }

    }

    @RabbitListener(queues = Constants.DELAY_QUEUE)
    public void delayConsumer(String json, Channel channel, Message message) {
        String time = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").format(new Date());
        LogUtil.debug(log, "延时消息处理：时间：{} 消息体：{} threadlocal：{}", time, json, Thread.currentThread().getName());
        System.out.println(json);
        try {
            // 配置文件中为手动应答，所以这里需 应答
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            try {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }

    }

    @RabbitListener(queues = Constants.GENERAL_QUEUE)
    public void generalConsumer(String json, Channel channel, Message message) {
        String time = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").format(new Date());
        LogUtil.debug(log, "会异常消息处理：时间：{} 消息体：{} threadlocal：{}", time, json, Thread.currentThread().getName());
        System.out.println(json);
        try {
            Integer.valueOf("aa");
            // 配置文件中为手动应答，所以这里需 应答
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
           log.info("发生异常");
            try {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }

    }

}
