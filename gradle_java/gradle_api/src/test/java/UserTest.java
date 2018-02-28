import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Cheng Yufei
 * @create 2017-11-10 下午11:13
 **/
@Data
@AllArgsConstructor
public class UserTest {

    private Integer id;

    private String name;



    private Integer age;

    public UserTest(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public UserTest(String name) {
        this.name = name;
    }
}
