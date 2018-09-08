package cyf.gradle.newredis.configuration;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author Cheng Yufei
 * @create 2017-10-30 16:32
 **/
@Configuration
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")

    private int port;

    private int timeout;

    @Bean
    public KeyGenerator wiseKeyGenerator() {

        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass());
                sb.append(method.getName());
                for (Object obj : params) {
                    sb.append(obj.toString());

                }
                return sb.toString();
            }
        };
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {

        GenericFastJsonRedisSerializer serializer = new GenericFastJsonRedisSerializer();

        RedisSerializationContext.SerializationPair serializationPair = RedisSerializationContext.SerializationPair.fromSerializer(serializer);

        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                //不存储空值
                .disableCachingNullValues()
                //有效时间
                .entryTtl(Duration.ofMinutes(1))
                // 将存储的value值序列化，否则有乱码
                .serializeValuesWith(serializationPair);


        Map<String, RedisCacheConfiguration> map = new HashMap<>(1);
        //针对Set种不同cache存储空间，对应各自的 configuration 配置
        map.put("my_redis_cache", configuration);

        RedisCacheManager cacheManager = RedisCacheManager.builder(factory)

                //此值需与方法缓存注解的value值一致；可设置多种cache名存储空间（my_redis_cache_2等），存储不同需求的值
                .initialCacheNames(Sets.newHashSet("my_redis_cache"))
                .withInitialCacheConfigurations(map)
                .build();

        return cacheManager;
    }


   /* private void setSerializer(StringRedisTemplate template) {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);
    }*/

    /**
     * redistemplate key、value 序列化
     * @param redisTemplate
     * @return
     */
   @Bean
   public RedisTemplate serializeRedistemplate(RedisTemplate redisTemplate) {
       GenericFastJsonRedisSerializer serializer = new GenericFastJsonRedisSerializer();

       redisTemplate.setKeySerializer(serializer);
       redisTemplate.setValueSerializer(serializer);
       return redisTemplate;
   }

    @Bean(name = "threadPoolExecutor")
    public ThreadPoolExecutor getThreadPoolExecutor() {

        ThreadFactory build = new ThreadFactoryBuilder().setNameFormat("demo_pool_%d").build();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 5,
                60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(20), build, new ThreadPoolExecutor.AbortPolicy());

        return threadPoolExecutor;
    }

    @Bean(name="scheduleThreadPool")
    public ScheduledThreadPoolExecutor getScheduleThread() {

        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("schedule_thread_%d").build();
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2, threadFactory, new ThreadPoolExecutor.AbortPolicy());
        return executor;
    }
}
