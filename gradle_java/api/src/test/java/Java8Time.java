import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Test;

import java.text.ParseException;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

        //格式化
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String format = time.format(pattern);
        System.out.println(format);

        TemporalAccessor parse = pattern.parse("2018-07-02 12:12:12");
        LocalDateTime from = LocalDateTime.from(parse);

        //LocalDate 转 Date
        Date date = Date.from(from.atZone(ZoneId.systemDefault()).toInstant());
        System.out.println("LocalDate 转 Date:   " + date);

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
        System.out.println("Instant 与 Date 转换: " + date1);

        //Date 转 LocalDate
        Date date2 = new Date();
        LocalDate localDate1 = LocalDate.from(date2.toInstant().atZone(ZoneId.systemDefault()));
        System.out.println(localDate1.getYear() + "    " + localDate1.getMonthValue() + "      " + localDate1.getDayOfMonth());

    }

    @Test
    public void format() throws ParseException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime.now().format(formatter);
        LocalDateTime.parse("", formatter);

        Date date = FastDateFormat.getInstance("yyyy-MM-dd").parse("1993-09-29");

        /**
         *  线程安全类：DateTimeFormatter、FastDateFormat
         *
         *   SimpleDateFormat 线程不安全：
         *
         *   1. 不能设置static 【 format(Date date) 方法 中  calendar.setTime(date); calendar是共享的，A线程设置完后，还没运行完此方法，B线程进入，设置成其他时间,A时间计算错误】
         *
         *   2. 设置static 同时需要加锁
         *
         *   3. 使用ThreadLocal 实现：
         *          ThreadLocal<SimpleDateFormat> sdf = new ThreadLocal<SimpleDateFormat>() {
         *             @Override
         *             protected SimpleDateFormat initialValue() {
         *                 return new SimpleDateFormat("yyyy-MM-dd");
         *             }
         *         };
         */

    }


    @Test
    public void theConstellation() throws ParseException {
        List<String> starList = Arrays.asList("摩羯座", "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座");
        int[] DayArr = {21, 20, 21, 21, 22, 22, 23, 24, 24, 24, 23, 22};

        String dateStr = "1993-03-29";

        LocalDate localDate = LocalDate.parse(dateStr);
        int index = localDate.getMonthValue() - 1;

        if (localDate.getDayOfMonth() >= DayArr[index]) {
            index = index + 1;
        }
        System.out.println(starList.get(index));


        Calendar cal = Calendar.getInstance();
        cal.setTime(FastDateFormat.getInstance("yyyy-MM-dd").parse(dateStr));
        int month = cal.get(Calendar.MONTH) + 1;
        int index2 = month;
        int day = cal.get(Calendar.DATE);
        // 所查询日期在分割日之前，索引-1，否则不变
        if (day < DayArr[month - 1]) {
            index2 = index2 - 1;
        }
        System.out.println(starList.get(index2));
    }

}
