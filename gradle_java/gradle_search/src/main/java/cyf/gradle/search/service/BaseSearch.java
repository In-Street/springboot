package cyf.gradle.search.service;

import com.google.gson.Gson;
import cyf.gradle.search.enums.IndexType;
import cyf.gradle.search.plugin.DeleteByQuery;
import cyf.gradle.util.FastJsonUtils;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.*;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author Cheng Yufei
 * @create 2017-11-01 10:58
 **/
@Service
@Slf4j
public class BaseSearch  {

    @Autowired
    private JestClient jestClient;


    public DocumentResult add(Object object, String id, String index, String type) throws IOException {
        Index in = new Index.Builder(FastJsonUtils.toJSONString(object))
                .id(id)
				.index(index)
                .type(type)
                .build();

        DocumentResult execute = jestClient.execute(in);

        log.info("保存数据返回：{}", execute.getJsonString());
        return execute;
    }

    public BulkResult adds(List actions, String index, String type) throws IOException {
        Bulk bulk = new Bulk.Builder()
                .defaultIndex(index)
                .defaultType(type)
                .addAction(actions)
                .build();
        String data = bulk.getData(new Gson());
        log.debug("data={}", data);
        BulkResult execute = jestClient.execute(bulk);
        log.info("保存数据返回：{}", execute.getJsonString());
        return execute;
    }


    public DocumentResult delete(Integer id, String index, String type) throws IOException {
        Delete delete = new Delete.Builder(id + "")
                .index(index)
                .type(type)
                .build();
        DocumentResult execute = jestClient.execute(delete);
        log.info("数据返回：{}", execute.getJsonString());
        return execute;
    }

    public JestResult deleteByIds(List ids, String index, String type) throws IOException {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders
                .boolQuery()
                .must(index.equals(IndexType.user.getIndex())&& type.equals(IndexType.user.getType())? QueryBuilders.termsQuery("uid", ids):QueryBuilders.termsQuery("id", ids));
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(boolQueryBuilder);

        cyf.gradle.search.plugin.DeleteByQuery  deleteByQuery = new cyf.gradle.search.plugin.DeleteByQuery
                .Builder(searchSourceBuilder.toString())
                .addIndex(index)
                .addType(type)
                .build();

        JestResult execute = jestClient.execute(deleteByQuery);
        log.info("数据返回：{}", execute.getJsonString());
        return execute;
    }


}
