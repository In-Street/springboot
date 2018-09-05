package cyf.gradle.dao.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author Cheng Yufei
 * @create 2018-06-15 13:43
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UDaily {

    private Integer id;

    private Integer endId;

    private Integer pageCnt;

    private Integer value;

    public UDaily(Integer id, Integer value) {
        this.id = id;
        this.value = value;
    }

    private List<ClubUserDailyStat> list;
}
