package cyf.gradle.searchdata.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Cheng Yufei
 * @create 2017-12-12 18:37
 **/
@Service
@Slf4j
public class SearchDataService {

    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    public void get() {
        Map miranda = elasticsearchTemplate.getSetting("miranda");
        System.out.println();
    }


}
