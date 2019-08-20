import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.cxytiandi.encrypt.util.AesEncryptUtils;
import com.google.common.collect.Maps;
import com.google.common.primitives.Ints;
import cyf.gradle.api.Enums.PushType;
import cyf.gradle.api.Enums.UserTest;
import cyf.gradle.api.service.CommandOrder;
import cyf.gradle.api.service.CommandUser;
import cyf.gradle.base.dto.LomDto;
import cyf.gradle.base.dto.UserDto;
import cyf.gradle.base.model.Response;
import cyf.gradle.dao.model.SysRole;
import cyf.gradle.dao.model.User;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanCopier;
import reactor.core.publisher.Flux;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Cheng Yufei
 * @create 2017-12-17 上午11:15
 **/

public class TestHome {


    @Test
    public void md5() throws Exception {
        String str = "123456";
        Md5Hash md5Hash = Md5Hash.fromHexString(str);
        String hex = md5Hash.toHex();
//        System.out.println(hex);
        String decrypt = AesEncryptUtils.aesDecrypt("5JvVDvyJCOSdxCiN0TicbA==", "abcdef0123456789");
        System.out.println(decrypt);
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
    public void bytes() {
        //一个用户ID 8个字节，100万用户ID 7M
        byte b = 8;
        int i = 1000000 * 8 / 1024 / 1024;
        int M = 1000000 << 3 >> 10 >> 10;
        System.out.println(i + "========" + M);

        System.out.println(2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2 * 2);
        System.out.println(Math.pow(2, 10));
        System.out.println(Math.sqrt(8));

    }

    @Test
    public void rendom() {
        String s = RandomUtil.randomStringUpper(6);
        System.out.println(s);

    }

    @Test
    public void command() {
        CommandOrder order = new CommandOrder("Order");
        CommandUser user = new CommandUser("User");


        String orderExecute = order.execute();
        System.out.println("order-execute -" + orderExecute);

        String userExecute = user.execute();
        System.out.println("user-execute - " + userExecute);
    }

    @Test
    //try/cache 异常
    @SneakyThrows(value = {FileNotFoundException.class, IOException.class})
    public void threadT() {
        //CPU 核数，用于线程池核心数的设定
        int i = Runtime.getRuntime().availableProcessors();
        System.out.println(i);

        //User 类添加@Builder,设置对象属性
        User build = User.builder().id(100).username("程").pwd("宇飞").build();
        User build1 = User.builder().build();
        System.out.println(build1);

        File file = new File("D:/A.txt");
        //调用资源关闭
        @Cleanup
        FileOutputStream outputStream = new FileOutputStream(file);
        String str = "哈哈哈";
        outputStream.write(str.getBytes());
        throw new FileNotFoundException("not found");
    }

    @Test
    public void idea() {
        List<Integer> integers = Ints.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        for (int i = 0; i < integers.size(); i++) {
            //条件断点（设定i==8 时，会在循环到8时进入断点）
            System.out.println(integers.get(i));
        }
        //Drop Frame 重新走
        m1();
    }

    private void m1() {
        System.out.println("m1");
        m2();
    }

    private void m2() {
        System.out.println("m2");
    }

    @Test
    public void newObject() {
        //匿名内部类方式初始化不推荐使用
        new User() {
            {
                setId(1);
            }
        };
    }

    /**
     * 深克隆、浅克隆
     *
     * @throws CloneNotSupportedException
     */
    @Test
    public void cloneObject() throws CloneNotSupportedException {

        /**
         * 1.clone(): 不调用构造器
         * 2.基本类型拷贝包括String，生成新对象，与原对象互不干扰
         * 3.如果User中有其他引用对象(SysRole)，clone后进行属性修改，原来对象的值也改变，俩者操作的是同一个对象，属于浅克隆
         */
        SysRole sysRole = SysRole.builder().id(1000).role("A角色").build();

        User user = User.builder().id(1).username("Tay").sysRole(sysRole).build();
        System.out.println(user + "     id:" + user.getId() + "     name:" + user.getUsername() + "     sysrole:" + user.getSysRole() + "    " + user.getSysRole().getId());

        User clone = user.clone();
        clone.setId(2);
        clone.setUsername("lor");
        SysRole role = clone.getSysRole();
        role.setId(2000);
        role.setRole("B角色");
        clone.setSysRole(role);
        System.out.println(user.getSysRole() + "    " + user.getSysRole().getId() + "    " + user.getId());
        System.out.println(clone + "     id:" + clone.getId() + "     name:" + clone.getUsername() + "     sysrole:" + clone.getSysRole() + "    " + clone.getSysRole().getId());

        //浅克隆打印结果：
        /**
         *      cyf.gradle.dao.model.User@1412c2f     id:1     name:Tay     sysrole:cyf.gradle.dao.model.SysRole@82ba1e    1000
         *      cyf.gradle.dao.model.SysRole@82ba1e    2000
         *      cyf.gradle.dao.model.User@13b6d03     id:2     name:lor     sysrole:cyf.gradle.dao.model.SysRole@82ba1e    2000
         */

        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------");

        /**
         * 深克隆: 将属性中的SysRole也实现Cloneable接口
         *        克隆User对象时候同时也许克隆SysRole对象，这样 clone后SysRole是新对象
         */
        SysRole deepSysRole = SysRole.builder().id(3000).role("C角色").build();
        User deepUser = User.builder().id(3).username("Can").sysRole(deepSysRole).build();
        System.out.println(deepUser + "     id:" + deepUser.getId() + "     name:" + deepUser.getUsername() + "     sysrole:" + deepUser.getSysRole() + "    " + deepUser.getSysRole().getId());

        User deepClone = deepUser.clone();
        deepClone.setId(2);
        deepClone.setUsername("lor");
        SysRole deepRole = deepUser.getSysRole().clone();
        deepRole.setId(4000);
        deepRole.setRole("D角色");
        deepClone.setSysRole(deepRole);
        System.out.println(deepUser.getSysRole() + "    " + deepUser.getSysRole().getId());
        System.out.println(deepClone + "     id:" + deepClone.getId() + "     name:" + deepClone.getUsername() + "     sysrole:" + deepClone.getSysRole() + "    " + deepClone.getSysRole().getId());
        //深克隆打印结果：
        /**
         * cyf.gradle.dao.model.User@15f2bb7     id:3     name:Can     sysrole:cyf.gradle.dao.model.SysRole@1035e27    3000
         * cyf.gradle.dao.model.SysRole@1035e27    3000
         * cyf.gradle.dao.model.User@c64813     id:2     name:lor     sysrole:cyf.gradle.dao.model.SysRole@cf72fd    4000
         */


        /**
         *  1. 构造方法有复杂操作时，通过clone'比new对象效率高。
         *  2. 继承关系比较深的类,new 对象时构造函数调用链较长，耗时,应使用clone()
         */
    }

    /**
     * 属性拷贝推荐使用：
     * BeanUtils.copyProperties() ：org.springframework.beans.BeanUtils 包下，而不是Apache包下的
     * BeanCopier :  org.springframework.cglib.beans.BeanCopier 包下
     */
    @Test
    public void beanCopy() {

        //pwd属性：名称相同但类型不同，不会复制
        User user = User.builder().id(3).username("Can").pwd("11").build();
        UserDto dto = new UserDto();
        BeanUtils.copyProperties(user, dto);
        System.out.println(dto);

        //pwd属性：名称相同但类型不同，不使用转换器的情况下不会复制
        BeanCopier beanCopier = BeanCopier.create(User.class, UserDto.class, false);
        beanCopier.copy(user, dto, null);
        System.out.println(dto);

        //使用自定义转换器可操作 名称相同但类型不同的属性拷贝
        BeanCopier copier = BeanCopier.create(User.class, UserDto.class, true);
        // o源对象属性set的值，aClass目标对象属性类，o1 目标对象setter方法名
        copier.copy(user, dto, (o, aClass, o1) -> {
            if (o1.equals("setPwd")) {
                return Integer.valueOf((String) (o));
            }
            //如果User类中没有excessProperty属性，UserDto中有,转换器中不会执行
            if (o1.equals("setExcessProperty")) {
                return "excessProperty";
            }
            return o;
        });
        System.out.println(dto);
    }

    @Test
    public void sys() {
        int i = Runtime.getRuntime().availableProcessors();
        System.out.println(i);

        AtomicInteger count = new AtomicInteger(1);
        // 1
        System.out.println(count.get());
        // 1  2
        System.out.println(count.getAndIncrement() + "------" + count.get());
        // 3  3
        System.out.println(count.incrementAndGet() + "------" + count.get());

        System.out.println(MessageFormat.format("时间：{0}", System.currentTimeMillis()));
    }

    @Test
    public void bifunction() {
        //双入参单出
        BiFunction<String, String, String> function = (k, v) -> {
            return k + v;
        };

        String apply = function.apply("A", "B");
        System.out.println(apply);


//        EnumMap<String, String> map = new EnumMap();


    }

    @Test
    public void stream1() throws IOException {
        Path path = Paths.get("D:/A.txt");
        Stream<String> lines = Files.lines(path);
        System.out.println(lines.collect(Collectors.toList()));

        String collect = Stream.of("A", "B").collect(Collectors.joining(","));
        //A,B
        System.out.println(collect);


        IntStream intStream = IntStream.of(2, 1, 4, 3, 5);
        int max = intStream.min().getAsInt();
        System.out.println(max);

        Comparator comparator = (o1, o2) -> (int) o1 > (int) o2 ? 1 : -1;
        Object o = Stream.of(2, 1, 4, 3, 5).max(comparator).get();
        System.out.println(o);

    }


    @Test
    public void arrays() {
        Integer[] i = new Integer[]{1, 2, 3};
        List<Integer> ints = Arrays.asList(i);
        //转换的为 Arrays内部类 ArrayList ，对元素操作：java.lang.UnsupportedOperationException
        ints.add(4);
        System.out.println(ints);
    }

    @Test
    public void LongAdder() {
        int i = ThreadLocalRandom.current().nextInt(5);
        System.out.println(i);

//        LongAdder 对象，比 AtomicLong 性能更好（减少乐观锁的重试次数）。
        LongAdder longAdder = new LongAdder();
        longAdder.add(1);
        longAdder.add(1);
        longAdder.add(2);
        int intValue = longAdder.intValue();
        //4
        System.out.println(intValue);
        long sum = longAdder.sum();
        //4
        System.out.println(sum);

    }

    @Test
    public void decrypt() throws Exception {
        String decrypt = AesEncryptUtils.aesDecrypt("F1vCMTfpq5w7jvWDzgbDrilBJg6+uCMtF5Ll1qnEqKwuAAgyjzeKtN4LS32mLkkhcE4O5IRXf1WEBrhyJMhS7yg3bQfqEnHH1wlmc3ArvEU=", "abcdef0123456789");
        System.out.println(JSONObject.parseObject(decrypt, Response.class));
    }


    @Test
    public void lombok() {

        //返回所有属性
//        Field[] declaredFields = lomDto.getClass().getDeclaredFields();

        //返回public修饰的属性
//        Field[] fields = lomDto.getClass().getFields();

        /**
         *  @Accessors(chain = true)
         *  @Accessors(fluent = true)  手动提供getter 方法 ，@Getter 不起作用
         *  @FieldDefaults(level = AccessLevel.PRIVATE) 默认属性修饰符
         */

        LomDto lomDto = new LomDto();
       /* @Accessors(chain = true)
        lomDto.setId(2).setNickname("Swift");*/

        /* @Accessors(fluent = true)*/
        lomDto.id(3).name("Candice");
        System.out.println(lomDto);

        LomDto dto = null;
        System.out.println(dto);
    }

    @Test
    public void enums() {

        EnumMap<PushType, String> enumMap = new EnumMap<>(PushType.class);
        enumMap.put(PushType.CHAT, "");
        enumMap.put(PushType.MATCH_SUCCESSFUL, "");
    }

    @Test
    public void constants() {
        Integer i1 = new Integer(40);
        Integer i6 = new Integer(40);
        Integer i2 = new Integer(0);

        Integer i3 = 40;

        Integer i4 = 40;

        Integer i5 = 0;

        System.out.println("i3==i1      " + (i3 == i1));

        System.out.println("i3 == i4+i5     " + (i3 == i4 + i5));

        System.out.println("i1== i4+i5     " + (i1 == i4 + i5));

        System.out.println("i1== i6+i2     " + (i1 == i6 + i2));


        //Double\Float 没有常量池
        Float d1 = 2.1f;
        Float d2 = 2.1f;
        System.out.println(d1 == d2);

    }
}

/**
 * 回顾点：
 * BiFunction
 * getAndIncrement
 * BeanUtils.copyProperties
 * 深克隆、浅克隆
 * localDate.plus(1, ChronoUnit.WEEKS)
 * LocalTime
 * DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
 * LocalDateTime
 * Date date = Date.from(from.atZone(ZoneId.systemDefault()).toInstant());
 * LocalDate localDate1 = LocalDate.from(date2.toInstant().atZone(ZoneId.systemDefault()));
 * ChronoUnit.DAYS.between(from, time);
 * Optional.ofNullable  orElse  ifPresent
 * stream flatMap
 * Multiset
 * HashMultimap
 * HashBiMap
 * HashBasedTable
 * Joiner Splitter
 * ImmutableMap.of("B", 1, "C", null);
 * Iterables.filter transform any concat frequency elementsEqual
 * Sets.difference union  intersection
 * EnumUtils
 * Collections.max
 * Ordering.natural().greatestOf(integers, 2);
 * Range.closed
 * Lists.partition
 * <p>
 * FutureTask futureTask = new FutureTask(new MyCallable());
 * FutureTask 为 Future接口的实现，创建对象后可以作为submit参数，也可取到执行的结果 threadPoolExecutor.submit(futureTask);
 *
 * @HystrixCommand(fallbackMethod = "fallback",
 * commandProperties = {
 * @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD"),
 * },
 * threadPoolProperties = {
 * @HystrixProperty(name = "coreSize", value = "5"),
 * @HystrixProperty(name = "maxQueueSize", value = "7"),
 * @HystrixProperty(name = "keepAliveTimeMinutes", value = "5"),
 * @HystrixProperty(name = "queueSizeRejectionThreshold", value = "50")
 * })
 * 同步利用模块用自己的线程池执行方法，出错不会影响全局线程池； 若要异步，方法返回结果需为 AsyncResult
 * <p>
 * 事件机制：EventBus 、 AsyncEventBus post / @Subscribe 业务处理
 * <p>
 * Guava-cache ：
 * @Bean(name = "asyncRefreshLoadingCache")
 * CacheBuilder.newBuilder(). refreshAfterWrite(1, TimeUnit.MINUTES).build 重写load 、reload方法{MoreExecutors.listeningDecorator(getThreadPoolExecutor())}
 * <p>
 * 异步回调：
 * ListenableFuture<String> listenableFuture = MoreExecutors.listeningDecorator(threadPoolExecutor).submit(new Callable)
 * Futures.addCallback(listenableFuture, new FutureCallback<String>(){override onSuccess(){}},threadPoolExecutor)
 */
