import cyf.gradle.rabbitmq.MqApplication;
import cyf.gradle.rabbitmq.controller.ExchangeReceiver;
import cyf.gradle.rabbitmq.controller.ExchangeSender;
import cyf.gradle.rabbitmq.controller.FirstSender;
import cyf.gradle.rabbitmq.controller.SecondSender;
import cyf.gradle.rabbitmq.modal.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Cheng Yufei
 * @create 2017-09-18 16:58
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MqApplication.class)
public class RabbitMqTest {

    @Autowired
    private FirstSender firstSender;

    @Autowired
    private SecondSender secondSender;

    @Autowired
    private ExchangeSender exchangeSender;

    @Test
    public void first() {
        firstSender.send();
    }

    @Test
    public void second() {
        for (int i = 0; i < 2; i++) {

            secondSender.send(new User("Taylor Swift"));
        }
    }

    @Test
    public void exchange() {
        //只能匹配到一个 toptic.#    结果一个接受者 ：exchange_Receiver_2  : exchange_Sender_2 taylor swift
//        exchangeSender.send2();

        //匹配到topic.# 和 topic.message  结果两个接受者: exchange_Receiver_2  : exchange_Sender_1 chengyufei, exchange_Receiver_1  : exchange_Sender_1 chengyufei
        exchangeSender.send();

    }
}
