package cyf.gradle.pay.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Cheng Yufei
 * @create 2018-09-21 17:46
 **/
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "wx.pay")
public class PayConfig {

    private String appId;
    private String mchId;
    private String notifyUrl;
    private String kerPrivate;
}
