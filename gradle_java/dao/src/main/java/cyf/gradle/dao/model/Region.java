package cyf.gradle.dao.model;

import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 
 */
@Accessors(fluent = true)
@Setter
public class Region implements Serializable {
    private Integer id;

    private String name;

    private Date createTime;

    private static final long serialVersionUID = 1L;


    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getCreateTime() {
        return createTime;
    }
}