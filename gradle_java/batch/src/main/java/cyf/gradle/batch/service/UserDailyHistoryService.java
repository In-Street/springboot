package cyf.gradle.batch.service;

import com.google.common.base.Stopwatch;
import cyf.gradle.dao.mapper.UDUserDailyMapper;
import cyf.gradle.dao.model.ClubUserDailyStat;
import cyf.gradle.dao.model.UDaily;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * @author Cheng Yufei
 * @create 2018-06-15 10:41
 **/
@Service
@Slf4j
public class UserDailyHistoryService {

    @Autowired
    private UDUserDailyMapper udUserDailyMapper;

//    @Transactional(rollbackFor = Exception.class)
    public void copyToHistoryAndUpdate() {
      /*  Date now = new Date();
        String yes = FastDateFormat.getInstance("yyyy-MM-dd").format(new Date(now.getTime() - 86400000L));
        log.info("开始处理user_daily_stat 日期：[{}] 的数据 ", yes);
        Integer maxIdDaily = udUserDailyMapper.selectMaxIdDaily(yes);
        Integer minIdDaily = udUserDailyMapper.selectMinIdDaily(yes);
        if (Objects.isNull(maxIdDaily) || Objects.isNull(minIdDaily)) {
            log.info("处理user_daily_stat 日期：[{}] 无数据结束任务 ", yes);
            return;
        }*/
        Stopwatch stopwatch = Stopwatch.createStarted();
        int maxIdDaily = 108362;
        int minIdDaily = 6584;
        int idDifference = maxIdDaily - minIdDaily;
        int batchSize = 1000;
        int pageNum = idDifference / batchSize;
        pageNum = idDifference % batchSize == 0 ? pageNum : pageNum + 1;

        UDaily uDaily = new UDaily();

        for (int i = 0; i < pageNum; i++) {
            uDaily.setId(i * batchSize + minIdDaily);
            uDaily.setEndId(uDaily.getId() + batchSize > maxIdDaily ? maxIdDaily + 1 : uDaily.getId() + batchSize);
            log.info("分批处理user_daily_stat id：[{}] - [{}]", uDaily.getId(), uDaily.getEndId());
            List<ClubUserDailyStat> clubUserDailyStats = udUserDailyMapper.selectByIDRangeDaily(uDaily);
            if (clubUserDailyStats.isEmpty()) {
                continue;
            }
            udUserDailyMapper.insertHistoryList(clubUserDailyStats);
            log.info("user_daily_stat 数据完成user_daily_stat_history 插入");

           /* Map<Integer, List<ClubUserDailyStat>> map = clubUserDailyStats.parallelStream().filter(d -> d.getBeLikedNum() > 0).collect(Collectors.groupingBy(ClubUserDailyStat::getUserId));
            if (map.size() == 0) {
                continue;
            }
            List<UDaily> uDailies = Lists.newArrayList();
            for (Map.Entry<Integer, List<ClubUserDailyStat>> entry : map.entrySet()) {
                UDaily uDailyForUpdate = new UDaily(entry.getKey(),entry.getValue().get(0).getBeLikedNum());
                uDailies.add(uDailyForUpdate);
            }
            udUserDailyMapper.updateUser(uDailies);
            log.info("{} - 更新club_user: be_liked_num 成功", yes);*/
        }
        log.info("普通插入耗时：{} s",stopwatch.elapsed(TimeUnit.SECONDS));
//        log.info("完成处理user_daily_stat 日期：[{}] 的数据 ", yes);

    }

}
