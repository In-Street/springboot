package cyf.gradle.mail.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * 邮件服务
 *
 * @author Cheng Yufei
 * @create 2017-08-07 11:08
 **/
@Component
@Slf4j
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${mail.fromMail.addr}")
    private String from;

    @Autowired
    TemplateEngine templateEngine;

    /**
     * 简单邮件发送
     *
     * @param to
     * @param subject
     * @param text
     */
    public void sendSimpleMail(String to, String subject, String text) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setFrom(from);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(text);

        try {
            mailSender.send(simpleMailMessage);
            log.info("邮件已发送");
        } catch (Exception e) {
            log.error("邮件发送失败", e.getLocalizedMessage());
        }
    }

    /**
     * 附件邮件发送
     *
     * @param to
     * @param subject
     * @param text
     * @param filePath
     */
    public void sendAttachmentsMail(String to, String subject, String text, String filePath) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSubject(subject);
            helper.setText(text);
            helper.setFrom(from);
            helper.setTo(to);

            FileSystemResource fileSystemResource = new FileSystemResource(filePath);
            String name = fileSystemResource.getFilename();
            helper.addAttachment(name, fileSystemResource);

            mailSender.send(message);
            log.info("附件邮件发送成功");
        } catch (MessagingException e) {
            log.error("附件邮件发送失败", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    /**
     * 内嵌静态资源邮件发送
     * @param to
     * @param subject
     * @param text
     * @param filePath
     * @param id
     */
    public void sendInlineMail(String to, String subject, String text, String filePath,String id) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setText(text);
            helper.setSubject(subject);

            FileSystemResource resource = new FileSystemResource(new File(filePath));
            helper.addInline(id,resource);

            mailSender.send(mimeMessage);
            log.info("内嵌静态资源邮件发送成功");
        } catch (MessagingException e) {
            log.error("内嵌静态资源邮件发送失败",e.getLocalizedMessage());
        }

    }

    /**
     * 发送 html 模板
     * @param to
     * @param subject
     * @param id
     */
    public void  sendHtmlTemplate(String to, String subject,String id) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(mimeMessage,true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);

            Context context = new Context();
            context.setVariable("id",id);
            String emailContent = templateEngine.process("emailTemplate", context);

            //设置为true ，以 html 形式展示
            helper.setText(emailContent,true);
            mailSender.send(mimeMessage);

            log.info("发送html邮件成功");
        } catch (MessagingException e) {
            log.error("发送html邮件失败",e.getLocalizedMessage());
        }

    }
}
