package cyf.gradle.dao.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * @author 
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Kerr2 extends Region implements Serializable {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Kerr2 kerr2 = (Kerr2) o;
        return title.equals(kerr2.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }
}