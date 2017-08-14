package cyf.gradle.api.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import cyf.gradle.dao.mongodb.PrimaryMongoObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * mongo
 *
 * @author Cheng Yufei
 * @create 2017-08-08 18:29
 **/
@Repository
public class MongoService {
    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    MongoDbFactory factory;


    public void insert(String name) {
        mongoTemplate.insert(new PrimaryMongoObject(null, name));
    }

    public List<PrimaryMongoObject> findAll() {
        List<PrimaryMongoObject> list = mongoTemplate.findAll(PrimaryMongoObject.class);
        return list;
    }

    /**
     * 条件查询
     * @return
     */
    public List<PrimaryMongoObject> findByCondition() {
        Query query = new Query();

        //完全匹配
        Pattern pattern1 = Pattern.compile("^程宇飞$", Pattern.CASE_INSENSITIVE);
        //右匹配
        Pattern pattern2 = Pattern.compile("^.*飞$", Pattern.CASE_INSENSITIVE);
        //左匹配
        Pattern pattern3 = Pattern.compile("^程.*$", Pattern.CASE_INSENSITIVE);
        //模糊匹配
        Pattern pattern4 = Pattern.compile("^.*宇.*$", Pattern.CASE_INSENSITIVE);

        query.addCriteria(Criteria.where("name").regex(pattern4));
        List<PrimaryMongoObject> list = mongoTemplate.find(query, PrimaryMongoObject.class);
        return list;
    }

    public void del() {
        mongoTemplate.remove(new Query(), PrimaryMongoObject.class);
    }

    /**
     * 获取数据库的collection
     */
    public List<String> getCollections() {
        DB cyfdb = factory.getDb("cyfdb");
        Set<String> names = cyfdb.getCollectionNames();
        List<String> list = new ArrayList<>(names);
        return list;
    }

}
