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
    String AMQP_EXCHANGE_MSG = "amqp_exchange_msg";
    String AMQP_QUEUE_MSG = "amqp_queue_msg";
    String AMQP_ROUTING_KEY_MSG = "amqp_routing_key_queue_msg";

    /**
     * 延时
     */
    String AMQP_EXCHANGE_DEAD_LETTER = "amqp_exchange_dead_letter";
    String AMQP_ROUTING_KEY_DEAD_LETTER = "amqp_routing_key_dead_letter";
    String AMQP_ROUTING_KEY_DELAY= "amqp_routing_key_delay";

    String AMQP_QUEUE_DEAD_LETTER = "amqp_queue_dead_letter";

    String AMQP_QUEUE_DELAY = "amqp_queue_delay";
}
