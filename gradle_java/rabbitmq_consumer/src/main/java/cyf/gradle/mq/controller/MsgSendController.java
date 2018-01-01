package cyf.gradle.mq.controller;

import cyf.gradle.base.Constants;
import cyf.gradle.base.dto.queue.MessageDto;
import cyf.gradle.base.model.Response;
import cyf.gradle.util.FastJsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Cheng Yufei
 * @create 2017-12-31 下午7:44
 **/
@RestController
@Slf4j
public class MsgSendController {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @RequestMapping("/send")
    public Response send() {
        MessageDto messageDto = new MessageDto();
        messageDto.setPrimaryKey(1);
        messageDto.setType(MessageDto.Type.TOPIC);

        amqpTemplate.convertAndSend(Constants.AMQP_EXCHANGE_MSG,Constants.AMQP_ROUTING_KEY_MSG, FastJsonUtils.toJSONString(messageDto));
        return new Response();
    }

    @RequestMapping("/sendDelay")
    public Response sendDelay() {
        MessageDto messageDto = new MessageDto();
        messageDto.setPrimaryKey(2);
        messageDto.setType(MessageDto.Type.AUDIT_COMMENT);

        amqpTemplate.convertAndSend(Constants.AMQP_EXCHANGE_MSG,Constants.AMQP_ROUTING_KEY_MSG, FastJsonUtils.toJSONString(messageDto));
        return new Response();
    }
}
