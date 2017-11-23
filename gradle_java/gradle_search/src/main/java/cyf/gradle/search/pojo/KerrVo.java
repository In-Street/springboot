package cyf.gradle.search.pojo;

import io.searchbox.annotations.JestId;
import lombok.Data;

import java.util.Date;

/**
 * @author Cheng Yufei
 * @create 2017-11-20 10:51
 **/
@Data
public class KerrVo {

    @JestId
    private Integer id;

    private String title;

    private Date publishTime;

    private Tag tags;
}
