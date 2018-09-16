package cyf.gradle.base;

/**
 * @since 1.0
 */
public interface Constants {

    String WECHAT = "wechat_";
    String CACHE_ACCESS_TOKEN = WECHAT + "cache_access_token_";
    String CACHE_TICKET = WECHAT + "cache_ticket_";

    String USER_LOGIN_KEY = "video_user_login_key_";
    String USER_SEND_SMS_NUM_KEY = "user_send_sms_num_key_";
    String USER_SENN_SMS_KEY = "user_send_sms_key_";

    /**
     *  queue - 普通消息
     */
    String COMMON_EXCHANGE = "common_exchange";
    String COMMON_QUEUE = "common_queue";
    String COMMON_ROUTING_KEY = "common_routing_key";

    /**
     * 死信
     */
    String DEAD_LETTER_EXCHANGE_ = "dead_letter_exchange";
    String DEAD_LETTER_ROUTING_KEY = "dead_letter_routing_key";
    String DEAD_LETTER_QUEUE= "dead_letter_queue";
    /**
     * 延时
     */
    String DELAY_QUEUE = "delay_queue";
    String DELAY_ROUTING_KEY = "delay_routing_key";
}
