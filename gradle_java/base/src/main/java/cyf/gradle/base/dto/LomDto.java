package cyf.gradle.base.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

/**
 * @author Cheng Yufei
 * @create 2019-02-18 11:25
 **/
@Getter
@Setter
@ToString
@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LomDto {

    int id;

    String name;

    public String city;
}
