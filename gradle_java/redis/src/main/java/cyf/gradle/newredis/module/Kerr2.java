package cyf.gradle.newredis.module;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Kerr2 implements Serializable {
    private Integer id;

    private String title;

    private Date publishtime;

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "Kerr2{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}