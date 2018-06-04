import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * @author Cheng Yufei
 * @create 2017-11-10 下午11:13
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@ToString(exclude ={"id","tags"})
public class UserTest {

    private Integer id;

    private String name;

    private List<String> tags;

    private Integer age;
    private Date date;

    public UserTest(Date date) {
        this.date = date;
    }

    public UserTest(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public UserTest(Integer id, String name, List<String> tags) {
        this.id = id;
        this.name = name;
        this.tags = tags;
    }

    public UserTest(String name) {
        this.name = name;
    }

}
