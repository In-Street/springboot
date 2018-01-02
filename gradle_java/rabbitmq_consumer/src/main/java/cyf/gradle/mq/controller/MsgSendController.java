package cyf.gradle.mq.controller;

import cyf.gradle.base.Constants;
import cyf.gradle.base.dto.queue.MessageDto;
import cyf.gradle.base.model.Response;
import cyf.gradle.util.FastJsonUtils;
import cyf.gradle.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.AsyncAmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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
        LogUtil.debug(log, "发送正常消息：时间：{} 消息体：{} threadlocal：{}", time, jsonString,Thread.currentThread().getId());
        amqpTemplate.convertAndSend(Constants.AMQP_EXCHANGE_MSG, Constants.AMQP_ROUTING_KEY_MSG, jsonString);
        return new Response();
    }

    @RequestMapping("/sendDelay")
    public Response sendDelay() {
        MessageDto messageDto = new MessageDto();
        messageDto.setPrimaryKey(2);
        messageDto.setType(MessageDto.Type.AUDIT_COMMENT);
        String jsonString = FastJsonUtils.toJSONString(messageDto);
        String time = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").format(new Date());
        LogUtil.debug(log, "发送延时消息：时间：{} 消息体：{} threadlocal：{}", time, jsonString,Thread.currentThread().getId());
        amqpTemplate.convertAndSend(Constants.AMQP_EXCHANGE_MSG, Constants.AMQP_ROUTING_KEY_MSG, FastJsonUtils.toJSONString(messageDto));
        return new Response();
    }
}
