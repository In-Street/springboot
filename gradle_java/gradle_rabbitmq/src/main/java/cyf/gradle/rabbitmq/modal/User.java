package cyf.gradle.rabbitmq.modal;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Cheng Yufei
 * @create 2017-09-22 15:08
 **/
@Data
public class User implements Serializable{

    private String name;

    public User() {
    }

    public User(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                '}';
    }
}
