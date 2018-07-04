package cyf.gradle.dao.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 
 */
public class Kerr2 implements Serializable {
    private Integer id;

    private String title;

    private Date publishtime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getPublishtime() {
        return publishtime;
    }

    public void setPublishtime(Date publishtime) {
        this.publishtime = publishtime;
    }
}