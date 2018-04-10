import org.apache.shiro.crypto.hash.Md5Hash;
import org.assertj.core.util.Lists;
import org.junit.Test;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Cheng Yufei
 * @create 2017-12-17 上午11:15
 **/

public class TestHome {


    @Test
    public void md5() {
        String str = "123456";
        Md5Hash md5Hash = Md5Hash.fromHexString(str);
        String hex = md5Hash.toHex();
        System.out.println(hex);
    }

    @Test
    public void stream() throws IOException {
        List<String> list = Lists.newArrayList("cheng", "yu", "fei", "yu");
        System.out.println("-----------------原始数据------------------");
        System.out.println(list);


        //排除重复数据
        List<String> collect = list.stream().distinct().collect(Collectors.toList());
        System.out.println("----------------distinct-------------------");
        System.out.println(collect);


        List<String> collect1 = list.stream().limit(2).collect(Collectors.toList());
        System.out.println("----------------limit-------------------");
        System.out.println(collect1);


        //skip(n): 返回一个忽略前n个的stream
        List<String> collect2 = list.stream().skip(2).collect(Collectors.toList());
        System.out.println("-----------------skip------------------");
        System.out.println(collect2);


        //下标 <2 的元素添加id属性，其余不添加
        List<UserTest> userTests = Lists.newArrayList(new UserTest("a"), new UserTest("b"), new UserTest("c"), new UserTest("d"), new UserTest("e"));
        List<UserTest> userTestsRangeClose = IntStream.rangeClosed(0, userTests.size() - 1).mapToObj(i -> {
            UserTest userTest = userTests.get(i);
            if (i < 2) {
                userTest.setId(i + 1);
            }
            return userTest;
        }).collect(Collectors.toList());

        //range 不包含右面
        List<UserTest> userTestsRange = IntStream.range(0, userTests.size() - 1).mapToObj(i -> {
            UserTest userTest = userTests.get(i);
            if (i < 2) {
                userTest.setId(i + 1);
            }
            return userTest;
        }).collect(Collectors.toList());

        System.out.println("-------------UserTests--------------");
        System.out.println(userTests);
        System.out.println("-------------userTestsRangeClose--------------");
        System.out.println(userTestsRangeClose);
        System.out.println("-------------userTestsRange--------------");
        System.out.println(userTestsRange);

        //文件转 Stream
        List<String> collect3 = Files.lines(Paths.get("/Users/chengyufei/Downloads/b.txt"), Charset.defaultCharset()).collect(Collectors.toList());
//        System.out.println(collect3);

    }

    @Test
    public void number() {
        Integer num = 157700;
        String numStr = "";
        String numStr1 = "";

        DecimalFormat df = new DecimalFormat("0.0w");
        DecimalFormat df1 = new DecimalFormat("0w");
        DecimalFormat df2 = new DecimalFormat("0.0");
        //小于 1万具体显示 ； 1万 - 100万 保留一位小数 ； 大于等于100万 显示正数，没小数
       /* df1.setRoundingMode(RoundingMode.FLOOR);
        df.setRoundingMode(RoundingMode.FLOOR);*/
        if (num >= 10000 && num < 1000000) {
            numStr = df.format(num / 10000.0);
        } else if (num >= 1000000) {

            numStr = df1.format(num / 10000.0);
        } else {
            numStr = num.toString();
        }
        System.out.println(numStr);

        System.out.println("-----------------------------------");

        //小于1千具体显示；大于1千 保留一位小数k ；大于1万 保留一位小数w
        df2.setRoundingMode(RoundingMode.UP);
        if (num >= 10000) {
            numStr1 = df2.format(num / 10000d) + "W";
        } else if (num >= 1000) {
            numStr1 = df2.format(num / 1000d) + "K";
        } else {
            numStr1 = num.toString();
        }
        System.out.println(numStr1);

        String format = String.format("#%s#", "在吗");
        System.out.println(format);

    }

    @Test
    public void nulltest() {
        Integer i = null;
        // 能用valueOf 替代toString ，有效避免空指针

        String s = String.valueOf(i);
        System.out.println(s);

        String s1 = i.toString();
        System.out.println(s1);
    }

    @Test
    public void objects() {
//        UserTest userTest = new UserTest("aa");
        UserTest userTest = null;
        boolean b = Objects.nonNull(userTest);
        boolean b1 = Objects.isNull(userTest);
        System.out.println(b);
        System.out.println(b1);


        Objects.requireNonNull(userTest);
        userTest.setId(1);
        System.out.println(userTest);

    }

    @Test
    public void t1() {
        String[] strings = new String[]{"3","4"};
        Flux<String> just = Flux.just(strings);


    }

}
