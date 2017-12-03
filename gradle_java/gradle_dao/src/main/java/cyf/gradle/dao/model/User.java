package cyf.gradle.dao.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author 
 */
@Data

public class User implements Serializable {

    @ApiModelProperty(hidden = true)
    private Integer id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String pwd;

    @ApiModelProperty(hidden = true)
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