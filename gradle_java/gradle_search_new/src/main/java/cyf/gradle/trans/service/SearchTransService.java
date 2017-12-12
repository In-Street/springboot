package cyf.gradle.trans.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author Cheng Yufei
 * @create 2017-12-12 18:37
 **/
@Service
@Slf4j
public class SearchTransService {

    TransportClient transportClient = null;

    @PostConstruct
    void initLocalResource() {
        Settings settings = Settings.builder()
//                .put("cluster.name", "cyf-cluster")
                .put("client.transport.sniff", true).build();

        try {
            InetSocketTransportAddress[] addresses = new InetSocketTransportAddress[] {
                    new InetSocketTransportAddress(InetAddress.getByName("39.106.118.71"), 9300),
                new InetSocketTransportAddress(InetAddress.getByName("39.106.118.71"), 9301),
            } ;
            transportClient = new PreBuiltTransportClient(settings).
                    addTransportAddresses(addresses);
            System.out.println();
        } catch (UnknownHostException e) {
            log.error("ES服务器连接异常", e);
        }
    }

    public TransportClient getTransportClient() {
        return transportClient;
    }

    public boolean allTopicIntoEngine() {
        int batchSize = 100;
        SbTopic topic = new SbTopic();
//        topic.setPageCnt(batchSize);
        int recordcount = topicMapper.selectMaxId();
        int pageNum = recordcount / batchSize;
        pageNum = recordcount % batchSize == 0 ? pageNum : pageNum + 1;

        BulkRequestBuilder bulkRequest = null;
        for (int i = 0; i < pageNum; i++) {
            log.info("全量更新开始处理：ID[{}]", i);
            topic.setId(i * batchSize + 1);
            try {
                List<SbTopic> sbTopics = topicMapper.selectByIDRange(topic);
                if (sbTopics.size() == 0) {
                    continue;
                }
                bulkRequest = transportClient.prepareBulk();
                JsonConfig jsonConfig = getJsonConfig();
                TopicBo topicBo = null;
                for (int j = 0; j < sbTopics.size(); j++) {
                    topicBo = getTopicBo(sbTopics.get(j));
                    try {
                        bulkRequest.add(transportClient.prepareIndex("smallbronzetest","topictest")
                                .setSource(JSONObject.fromObject(topicBo, jsonConfig).toString()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            BulkResponse bulkResponse = bulkRequest.get();
            if (bulkResponse.hasFailures()) {
                log.error("全量更新处理出现错误，执行下一批次：ID[{}]", i);
            }
            log.info("全量更新处理结束：ID[{}]", i);
        }
        return true;
    }

    private TopicBo getTopicBo(SbTopic topic) {
        TopicBo bo = new TopicBo();
        BeanUtils.copyProperties(topic, bo);
        Integer videoId = topic.getVideoId();
        SbVideo video = videoMapper.selectByPrimaryKey(videoId);
        if (null != video) {
            //真实播放 + 虚拟播放
            bo.setPlayNum(video.getPlayNum() + video.getvPlayNum());
        }
        return bo;
    }


    private JsonConfig getJsonConfig() {
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(java.util.Date.class, new JsonValueProcessor() {
            private final String format = "yyyy-MM-dd";

            public Object processObjectValue(String key, Object value, JsonConfig arg2) {
                if (value == null)
                    return "1970-01-01";
                if (value instanceof java.util.Date) {
                    String str = new SimpleDateFormat(format).format((java.util.Date) value);
                    return str;
                }
                return value.toString();
            }

            public Object processArrayValue(Object value, JsonConfig arg1) {
                return null;
            }

        });
        jsonConfig.registerJsonValueProcessor(String.class, new JsonValueProcessor() {
            private final String format = "yyyy-MM-dd";

            public Object processObjectValue(String key, Object value, JsonConfig arg2) {

                if (key.equals("appDate") || key.equals("interRegDate") || key.equals("trialDate")) {
                    if (value == null || StringUtils.trimToEmpty((String) value).length() == 0)
                        return "1970-01-01";

                    try {
                        return DateFormatUtils.format(DateUtils.parseDate(StringUtils.trimToEmpty((String) value), new String[]{"yyyy-M-d", "yyyyMM-dd", "yyyyMMdd", "yyyy-MMdd", "yyyy-MM-dd", "MM d yyyy"}), format);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }

                if (value == null)
                    return "";
                return StringUtils.trimToEmpty((String) value);
            }

            public Object processArrayValue(Object value, JsonConfig arg1) {
                return null;
            }

        });
        return jsonConfig;
    }

}
