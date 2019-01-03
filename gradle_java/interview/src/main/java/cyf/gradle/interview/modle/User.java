package cyf.gradle.interview.modle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Cheng Yufei
 * @create 2018-06-25 下午9:49
 **/
@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class User {

    private Integer id;

    private String name;

    private String tag;

    private String city;

    private Integer money;

    public User(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
