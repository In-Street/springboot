package cyf.gradle.trans.module;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.util.Date;

/**
 * @author Cheng Yufei
 * @create 2017-11-20 10:51
 **/
@Data
@Document(indexName= "miranda",type="kerr_2",refreshInterval="-1")
public class Kerr2Vo {

    @Id
    private Integer id;

    @Field
    private String title;

    @Field
    private Date publishtime;

}
