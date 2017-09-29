package cyf.gradle.dao.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author 
 */
@Data
public class User implements Serializable {
    private Integer id;

    private String username;

    private String pwd;

    private List<SysRole> roleList;

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", roleList=" + roleList +
                '}';
    }
}