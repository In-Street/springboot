import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 * @author Cheng Yufei
 * @create 2018-07-03 15:05
 **/
public class Java8Time {

    @Test
    public void LocalDateTest() {

        //LocalDate
        LocalDate localDate = LocalDate.now();
        System.out.println("LocalDate.now " + localDate);

        LocalDate parse1 = LocalDate.parse("2018-06-26");
        System.out.println("LocalDate.parse: " + parse1);

        LocalDate of = LocalDate.of(2000, 7, 2);
        System.out.println("LocalDate.of :" + of);

        System.out.println(localDate.getDayOfYear() + "-------" + localDate.getDayOfMonth() + "-------" + localDate.getDayOfWeek());
        System.out.println(localDate.plusWeeks(1) + "-------" + localDate.plus(1, ChronoUnit.WEEKS));

        boolean leapYear = of.isLeapYear();
        System.out.println("是否是闰年:" + leapYear);

        //LocalTime
        LocalTime localTime = LocalTime.now();
        System.out.println("LocalTime.now : " + localTime);

        LocalTime plusHours = localTime.plusHours(2);
        System.out.println("localTime.plusHours: " + plusHours);

        LocalDateTime time = LocalDateTime.now();
        System.out.println(time);

        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String format = time.format(pattern);
        System.out.println(format);

        TemporalAccessor parse = pattern.parse("2018-07-02 12:12:12");
        LocalDateTime from = LocalDateTime.from(parse);

        //LocalDate 转 Date
        Date date = Date.from(from.atZone(ZoneId.systemDefault()).toInstant());
        System.out.println(date);

        //俩个日期的相差的年月日
        Period period = Period.between(parse1, localDate);
        System.out.println("相差：年：" + period.getYears() + "月：" + period.getMonths() + "日:" + period.getDays());
        //相差的秒数：
        Duration duration = Duration.between(from, time);
        System.out.println("相差：" + duration + "秒" + "----毫秒：" + duration.toMillis());
        //定义单位算差值
        long between = ChronoUnit.DAYS.between(from, time);
        long between1 = ChronoUnit.MINUTES.between(from, time);
        long between2 = ChronoUnit.WEEKS.between(from, time);
        System.out.println("相差：" + between + "-----" + between1 + "-----" + between2);


        //Instant 与 Date 转换
        Instant now = Instant.now();
        System.out.println("Instant.now:" + now);
        Date date1 = Date.from(now);
        System.out.println(date1);

        //Date 转 LocalDate
        Date date2 = new Date();
        LocalDate localDate1 = LocalDate.from(date2.toInstant().atZone(ZoneId.systemDefault()));
        System.out.println(localDate1.getYear()+"    "+localDate1.getMonthValue()+"      "+localDate1.getDayOfMonth());

    }
}
