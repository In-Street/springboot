package cyf.gradle.pay.service;

import com.egzosn.pay.common.bean.PayOrder;
import com.egzosn.pay.wx.api.WxPayConfigStorage;
import com.egzosn.pay.wx.api.WxPayService;
import cyf.gradle.pay.config.PayConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Cheng Yufei
 * @create 2018-09-21 16:38
 **/
@Service
@Slf4j
public class PayWxService {

    @Autowired
    private PayConfig payConfig;

    public void pay() {
        WxPayService wxPayService = Pay.INSTANCE.wxPayService;
        PayOrder order = new PayOrder();
        log.info("<=== pay ===>{}",payConfig.getAppId());
//        wxPayService.orderInfo(order);
    }

    private enum Pay {
        //
        INSTANCE;

        @Autowired
        private PayConfig payConfig;
        private WxPayService wxPayService;

        Pay() {
            log.info("<=== 初始化 ===>");
            WxPayConfigStorage payConfigStorage = new WxPayConfigStorage();
            payConfigStorage.setAppid(payConfig.getAppId());
            payConfigStorage.setMchId(payConfig.getMchId());
            payConfigStorage.setNotifyUrl(payConfig.getNotifyUrl());
            payConfigStorage.setKeyPrivate(payConfig.getKerPrivate());
            payConfigStorage.setSignType("MD5");
            payConfigStorage.setInputCharset("utf-8");
            wxPayService = new WxPayService(payConfigStorage);
        }
    }
}
