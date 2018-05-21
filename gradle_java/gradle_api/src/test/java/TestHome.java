import com.google.common.collect.Maps;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
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
//        String s1 = i.toString();

        UserTest userTest = new UserTest("aa");
//        UserTest userTest = null;
        boolean b = Objects.nonNull(userTest);
        boolean b1 = Objects.isNull(userTest);
        Objects.requireNonNull(userTest);
        userTest.setId(1);

        //返回空collection 避免size 时空指针
        List emptyList = Collections.EMPTY_LIST;
        System.out.println(emptyList.size());

    }

    @Test
    public void t1() {
        String[] strings = new String[]{"3", "4"};
        Flux<String> just = Flux.just(strings);
    }

    @Test
    public void t3() {

        //条件判定
        Predicate<Integer> p1 = k -> k > 2;
        boolean test = p1.test(1);
        System.out.println(test);

        //function 可接受一个参数， 前面输入 后面输出
        Function<String, Integer> f1 = Integer::valueOf;
        Integer apply = f1.apply("123");
        Function<String, String> f2 = k -> k + "45";
        Integer apply1 = f1.compose(f2).apply("123");
        System.out.println(apply1);

        //Supplier 返回任意类型的值，和function 不同的是 不接受参数
        Supplier<UserTest> s = UserTest::new;
        s.get();


        //consumer 执行单个参数的操作
        Consumer<UserTest> c1 = u -> System.out.println(u.getName());
        c1.accept(new UserTest("AA"));

        //parallelStream 并行流
        List<String> list = Lists.newArrayList("aaa", "bbb", "ccc");
        Optional<String> reduce = list.parallelStream().reduce((s1, s2) -> s1 + "#" + s2);
        System.out.println(reduce.get());

        Map<Integer, String> map = Maps.newHashMap();
        map.putIfAbsent(1, "a");
        map.putIfAbsent(2, "b");
        map.putIfAbsent(1, "a");

        map.forEach((k, v) -> {
            System.out.println(k + "---" + v);
        });

//        String computeIfAbsent = map.computeIfAbsent(3, v -> v + "$");
        String computeIfAbsent = map.computeIfPresent(2, (v, n) -> v + "$" + n);
        System.out.println(computeIfAbsent);


    }

    @Test
    public void t2() {

        List<Integer> list = Lists.newArrayList(2,4,7,10);
        System.out.println("1");
        List<Integer> collect = list.stream().map(s->{
            System.out.println("2");
            if (s>2) {
                return s;
            }else{
                return 0;
            }
        }).collect(Collectors.toList());
        System.out.println("3");
        System.out.println(collect);

    }

}
