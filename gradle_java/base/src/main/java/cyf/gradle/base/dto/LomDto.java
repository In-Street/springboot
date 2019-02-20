package cyf.gradle.base.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.Date;

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

    @JsonProperty(value = "nickname")
    String name;

    @JsonIgnore
    String city;

    Integer age;

    @JsonFormat(pattern = "yyyy-MM-dd")
    Date time;

    /**
     * @Accessors使用，序列化时 需提供getter方法，@Getter不起作用
     * @return
     */
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public Integer getAge() {
        return age;
    }

    public Date getTime() {
        return time;
    }
}
