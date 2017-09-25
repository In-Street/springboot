import cyf.gradle.rabbitmq.MqApplication;
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

    @Test
    public void first() {
        firstSender.send();
    }

    @Test
    public void second() {
        secondSender.send(new User("Taylor Swift"));
    }
}
