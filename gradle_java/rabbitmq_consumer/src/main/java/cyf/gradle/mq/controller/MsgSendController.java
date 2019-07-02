package cyf.gradle.mq.controller;

import cyf.gradle.base.Constants;
import cyf.gradle.base.dto.queue.MessageDto;
import cyf.gradle.base.model.Response;
import cyf.gradle.util.FastJsonUtils;
import cyf.gradle.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author Cheng Yufei
 * @create 2017-12-31 下午7:44
 **/
@RestController
@Slf4j
public class MsgSendController {

    @Autowired
    private AmqpTemplate amqpTemplate;
   /* @Resource
    private AsyncAmqpTemplate asyncAmqpTemplate;*/

    @RequestMapping("/send")
    public Response send() {
        MessageDto messageDto = new MessageDto();
        messageDto.setPrimaryKey(1);
        messageDto.setType(MessageDto.Type.TOPIC);
        String jsonString = FastJsonUtils.toJSONString(messageDto);
        String time = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").format(new Date());
        LogUtil.debug(log, "发送正常消息：时间：{} , 消息体：{}  , threadlocal：{}", time, jsonString,Thread.currentThread().getName());

        /**
         * 消息持久化：
         *         1.queue 设置为durable（new Queue 时默认是true）
         *         2.投递messages设置持久化 (发布消息默认持久)，确保在rabbitmq 服务挂掉后消息不会丢失
         */
        MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                return message;
            }
        };

        amqpTemplate.convertAndSend(Constants.COMMON_EXCHANGE, Constants.COMMON_ROUTING_KEY, jsonString,messagePostProcessor);
        return new Response();
    }

    @RequestMapping("/sendDelay")
    public Response sendDelay() {
        MessageDto messageDto = new MessageDto();
        messageDto.setPrimaryKey(2);
        messageDto.setType(MessageDto.Type.AUDIT_COMMENT);
        String jsonString = FastJsonUtils.toJSONString(messageDto);
        String time = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").format(new Date());
        LogUtil.debug(log, "发送延时消息：时间：{} ,消息体：{} ,threadlocal：{}", time, jsonString,Thread.currentThread().getName());
        amqpTemplate.convertAndSend(Constants.DEAD_LETTER_EXCHANGE, Constants.DEAD_LETTER_ROUTING_KEY, FastJsonUtils.toJSONString(messageDto));
        return new Response();
    }

    @RequestMapping("/sendGeneral")
    public Response sendGeneral() {
        MessageDto messageDto = new MessageDto();
        messageDto.setPrimaryKey(3);
        messageDto.setType(MessageDto.Type.TOPIC);
        String jsonString = FastJsonUtils.toJSONString(messageDto);
        String time = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").format(new Date());
        LogUtil.debug(log, "发送会异常的消息：时间：{} ,消息体：{} ,threadlocal：{}", time, jsonString,Thread.currentThread().getName());
        amqpTemplate.convertAndSend(Constants.COMMON_EXCHANGE, Constants.GENERAL_ROUTING_KEY, FastJsonUtils.toJSONString(messageDto));
        return new Response();
    }
}
