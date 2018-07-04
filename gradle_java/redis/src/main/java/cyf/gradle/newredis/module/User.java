package cyf.gradle.newredis.module;

import lombok.Data;

/**
 * @author Cheng Yufei
 * @create 2017-10-30 17:04
 **/
@Data
public class User {

    private Integer id;

    private String username;

    private String pwd;

    public User() {
    }

    public User(Integer id, String username, String pwd) {
        this.id = id;
        this.username = username;
        this.pwd = pwd;
    }
}
