package cyf.gradle.dao.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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