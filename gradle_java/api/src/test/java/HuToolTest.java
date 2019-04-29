import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.Lists;
import jodd.http.HttpRequest;
import jodd.http.HttpUtil;
import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Test;

import java.nio.charset.Charset;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * @author Cheng Yufei
 * @create 2019-04-29 11:31
 *
 * 文档： https://www.hutool.cn/docs/#/
 **/
public class HuToolTest {

    @Test
    public void convert() throws ParseException {

        /**
         * String数组转int数组
         */
        String[] strs = {"1", "2", "3"};
        Integer[] intArray = Convert.toIntArray(strs);
        Integer[] integers = Convert.convert(Integer[].class, strs);
        Stream.of(integers).forEach(s -> System.out.println(s));

        /**
         * 字符串转Date
         */
        String dateStr = "2019-04-29 16:11:23";
        Date date = Convert.toDate(dateStr);
        System.out.println(date);
        //Convert.convert(Class<T>, Object)方法可以将任意类型转换为指定类型
        Date convert = Convert.convert(Date.class, dateStr);
        System.out.println(convert);

        Date parse1 = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").parse(dateStr);
        System.out.println(parse1);

        LocalDateTime parse = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Date from = Date.from(parse.atZone(ZoneId.systemDefault()).toInstant());
        System.out.println(from);


        /**
         * 16进制 - 字符串
         */
        String str = "Hutool,学习";
        String s = Convert.toHex(str, Charset.forName("utf-8"));
        //Convert.hexToStr()
        System.out.println(s);

        /**
         * unicode - 汉字字符串
         */
        String s1 = Convert.strToUnicode("我，学习");
        System.out.println(s1);

        /**
         *转换时长单位，比如一个很大的毫秒，想获得这个毫秒数对应多少分
         */
        long time = 1555572826396L;
        long l = Convert.convertTime(time, TimeUnit.MILLISECONDS, TimeUnit.MINUTES);
        System.out.println(l);

        /**
         * 金额大小写转换
         */
        double money = 50123.35;
        String s2 = Convert.digitToChinese(money);
        System.out.println(s2);

        /**
         *集合随机取
         */
        ArrayList<String> strings = Lists.newArrayList("AA", "BB", "CC", "DD");
        String s3 = RandomUtil.randomEle(strings, strings.size());
        List<String> strings1 = RandomUtil.randomEles(strings, 2);
        System.out.println(strings1);

    }


}
