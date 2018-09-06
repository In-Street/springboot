package cyf.gradle.dao.mapper;

import cyf.gradle.dao.model.ClubUserDailyStat;
import cyf.gradle.dao.model.UDaily;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Cheng Yufei
 * @create 2018-06-15 10:48
 **/
public interface UDUserDailyMapper {

    Integer insertHistoryList(/*@Param("list") */List<ClubUserDailyStat> list);
    Integer insert( ClubUserDailyStat clubUserDailyStat);

    Integer selectMaxIdDaily(@Param("nowString") String nowString);

    Integer selectMinIdDaily(@Param("nowString") String nowString);

    List<ClubUserDailyStat> selectByIDRangeDaily(UDaily uDaily);

    Integer updateUser(@Param("targetList") List<UDaily> targetList);

    List<ClubUserDailyStat> selectByIDRange(@Param(("endId")) Integer endId);
}
