package cyf.gradle.base.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Cheng Yufei
 * @create 2018-08-30 下午10:23
 **/
@Getter
@Setter
@ToString
public class UserDto {

    private Integer id;

    private String username;

    private int pwd;

    private String excessProperty;

}
