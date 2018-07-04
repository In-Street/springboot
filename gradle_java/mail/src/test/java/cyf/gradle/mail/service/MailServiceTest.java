package cyf.gradle.mail.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MailServiceTest {


    @Autowired
    private MailService mailService;


    @Test
    public void sendSimpleMail() throws Exception {
        mailService.sendSimpleMail("chengyfanony@163.com","test mail","尽快回复");
    }

    @Test
    public void sendAttachmentsMail(){
        mailService.sendAttachmentsMail("chengyfanony@163.com","test attachment mail","附件邮件","D:/topvideo-api.log");
    }

    @Test
    public void sendInlineMail(){
        mailService.sendInlineMail("chengyfanony@163.com","test attachment mail","附件邮件","D:/heisebeijing-016.jpg","1");
    }

    @Test
    public void sendHtmlTemplate(){
        mailService.sendHtmlTemplate("chengyfanony@163.com","test attachment mail","001");
    }
}