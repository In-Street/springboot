import com.google.common.base.*;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.*;
import com.google.common.primitives.Ints;
import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.EmojiParser;
import cyf.gradle.api.Enums.LikeDto;
import cyf.gradle.api.Enums.PushType;
import cyf.gradle.api.Enums.RemoveMatchReason;
import cyf.gradle.api.Enums.UserTest;
import cyf.gradle.base.enums.PraiseEnum;
import cyf.gradle.util.EmojiRegexUtil;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Cheng Yufei
 * @create 2018-05-18 18:49
 **/
public class Study_xclub {

    @Test
    public void foreachReturn() {
        List list = Lists.newArrayList(1, 3, 4, 5, 6, 7, 8, 9, 10, 2);
        Map map = Maps.newHashMap();
        map.put(2, "B");

        list.forEach(s -> {
            if (!map.containsKey(s)) {
                return;
            }
//            System.out.println(map.get(s));
        });
        Object collect = list.parallelStream().map(s -> String.valueOf(s)).collect(Collectors.toList());
        List<Integer> list1 = Lists.newArrayList(1, 3, null);
        List<Integer> uids = list1.parallelStream().distinct().collect(Collectors.toList());
        System.out.println(uids);

    }

    @Test
    public void listRemoveList() {
        List list = Lists.newArrayList(1, 3, 4, 5, 6, 7, 8, 9, 10, 2);
        List list1 = Lists.newArrayList(1, 3, 4, 20, 21);
        list.removeAll(list1);
        System.out.println(list);
    }


    @Test
    public void jsonTemplate() {
        //language=JSON
        String str = "{\"id\":1,\"name\":\"str\"}";
        System.out.println(str);
    }


    @Test
    public void emoji() {

        String str = "\uD83D\uDE1C\uD83D\uDE29\uD83D\uDE29哈哈哈\uD83D\uDE29";
        if (EmojiRegexUtil.containsEmoji(str)) {
            System.out.println("含有");
        }
        String s1 = EmojiParser.parseToUnicode(str);
        System.out.println(s1);

        boolean onlyEmojis = EmojiManager.isOnlyEmojis(str);
        System.out.println(onlyEmojis);

        Emoji byUnicode = EmojiManager.getByUnicode("\uD83D\uDE1C");
        System.out.println(byUnicode);

        List<String> list = EmojiParser.extractEmojis(str);
        System.out.println(list);

        String[] strings = new String[list.size()];

        String s2 = StringUtils.replaceEach(str, list.toArray(strings), new String[]{"【动画表情】", "【动画表情】", "【动画表情】", "【动画表情】"});
//        System.out.println(s2);

        String s3 = StringUtils.replaceAll(str, list.toString(), "【动画表情】");
        System.out.println(s3);

        String str1 = "\uD83D\uDE1C\uD83D\uDE29\uD83D\uDE29\uD83D\uDE29";
        if (EmojiManager.isEmoji(str1)) {
            System.out.println("str1");

        }


       /* List<String> collect = list.parallelStream().map(e -> EmojiParser.parseToUnicode(e)).collect(Collectors.toList());
        System.out.println(collect);*/

    }

    @Test
    public void enumToStream() throws Exception {
        PraiseEnum[] values = PraiseEnum.values();
        List<PraiseEnum> noChatRemoveMatchReasons = Arrays.asList(values);

        Function<PraiseEnum, RemoveMatchReason> function = s -> new RemoveMatchReason(s.getCode(), s.getName());
        List<RemoveMatchReason> collect = Arrays.stream(values).map(function).collect(Collectors.toList());

        System.out.println(collect);
    }

    @Test
    public void parallelStream() {
        List<Integer> list = Lists.newArrayList(-1, 2, 3, 4);
        List<Integer> collect = list.parallelStream().filter(s -> !Objects.equals(s, -1)).collect(Collectors.toList());
        System.out.println(collect);

        String str = "{\"id\":1,\"name\":\"\"}";

    }


    @Test
    public void option() {
        LikeDto g1 = new LikeDto();
        g1.setId(1);
        g1.setNickname("a");

        LikeDto g2 = new LikeDto();
        g2.setId(2);
        g2.setNickname("b");

        LikeDto g3 = new LikeDto();
        g3.setId(3);
        g3.setNickname("c");


        List<LikeDto> groups = Lists.newArrayList();
        groups.add(g1);
        groups.add(g2);
        groups.add(g3);

        //Optional
        Function<List<LikeDto>, List<Integer>> function = list -> list.stream().map(LikeDto::getId).collect(Collectors.toList());
        List list = Optional.ofNullable(groups).map(function).orElse(Collections.EMPTY_LIST);
        LikeDto likeDto = new LikeDto();
//        LikeDto likeDto = null;
        //likeDto为null时返回一个Op1tional空对象
        Optional<Integer> integer1 = Optional.ofNullable(likeDto).map(LikeDto::getId);
        System.out.println("1" + integer1);

        //不存在返回null,避免报错
        Integer integer2 = Optional.ofNullable(likeDto).map(LikeDto::getId).orElse(null);
        System.out.println("2" + integer2);

        //ofNullable 允许传入null对象，不会报错，返回Optional.empty
        Optional<LikeDto> likeDto2 = Optional.ofNullable(likeDto);
        System.out.println("3" + likeDto2);

        //创建非空值的Optional，如果传入值为null会立即报NPE，而不是等到调用属性的时候
        Optional<LikeDto> likeDto1 = Optional.of(likeDto);
        System.out.println(likeDto1);

        //ifPresent 对象存在则执行Consumer,否则不做操作
        Optional.ofNullable(likeDto).ifPresent(s -> s.setNickname("if"));
        System.out.println("4" + likeDto);

        Map<String, LikeDto> map = Maps.newHashMap();
        map.put("aa", likeDto);
        Optional.ofNullable(map.get("aa")).ifPresent(s -> s.setAvatar("http://www.xxxxxx"));
        System.out.println("5---" + map.get("aa"));

        //Stream接口里带静态方法,用于创建Stream
        Stream<String> stringStream = Stream.of("a,b");
        Stream.empty();
        String[] strings = new String[]{"cyf", "taylor"};
        Object[] objects = Stream.of(strings).map(s -> s.toUpperCase()).toArray();
        for (Object object : objects) {
            System.out.println(object);
        }

        //IntStream 结合集合下标操作
        List<LikeDto> collect = IntStream.rangeClosed(0, groups.size() - 1).mapToObj(i -> {
            if (i == 2) {
                groups.get(i).setProvince("北京市");
            }
            return groups.get(i);
        }).collect(Collectors.toList());


        //flatMap: 类中属性含有集合的成员变量，flatMap中function第二个参数为stream 可直接操作属性的集合
        List<UserTest> userTests = Lists.newArrayList(new UserTest(1, "a", Lists.newArrayList("t1")), new UserTest(2, "b", Lists.newArrayList("t2")), new UserTest(3, "c", Lists.newArrayList("t3")));
        List<String> collect2 = userTests.stream().flatMap(s -> s.getTags().stream()).map(s -> s + "cc").collect(Collectors.toList());
        System.out.println();

        //peek: 生成一个包含原Stream的所有元素的新Stream，同时会提供一个消费函数（Consumer实例），新Stream每个元素被消费的时候都会执行给定的消费函数；
        Consumer<LikeDto> consumer = s -> s.setVipLevel(1000);
        Consumer<LikeDto> consumer_1 = s -> s.getId();
        List<LikeDto> collect1 = groups.stream().peek(consumer_1).collect(Collectors.toList());
        System.out.println();

        //汇聚，将上次汇聚的结果作为参数传入下次汇聚
        Integer integer = groups.stream().map(LikeDto::getId).reduce((i, j) -> i + j).get();
        //指定一个初始值2, 2+汇聚结果值
        Integer reduce = groups.stream().map(LikeDto::getId).reduce(2, (i, j) -> i + j);
//        System.out.println(integer + "----" + reduce);
    }

    @Test
    public void guava() throws IOException {
//        LikeDto likeDto = null;
        LikeDto likeDto = new LikeDto();
        likeDto.setNickname("Taylor");
        //参数校验
        LikeDto dto = Preconditions.checkNotNull(likeDto, "参数错误");
        System.out.println(dto);

        //Immutable：不可变集合
        String[] strings = new String[]{"a", "b", "c"};
        Stream<String> stream = ImmutableList.copyOf(strings).stream();
        ImmutableList.of(strings);

//        Multiset<String> set = LinkedHashMultiset.create();
        //Multiset:出现次数
        Multiset<String> set = HashMultiset.create();
        set.add("a");
        set.add("a");
        set.add("b");
        int a = set.count("a");
        System.out.println("Multiset -- " + a);

        //Multimap:同一个键值可存储多个value
        HashMultimap<Object, Object> multimap = HashMultimap.create();
        multimap.put("A", "程");
        multimap.put("A", "宇");
        multimap.put("A", "飞");
        Set<Object> set1 = multimap.get("A");
        System.out.println("HashMultimap -- " + set1);

        //键值可以是 ""、null
        Map<String, Integer> map = Maps.newHashMap();
        map.put("B", 1);
        map.put("", null);
        map.put(null, 2);


        //BiMap: 键值与value值都不可重复
        HashBiMap<Object, Object> hashBiMap = HashBiMap.create();
        hashBiMap.put("A", 1);
        hashBiMap.put("A", 2);
        hashBiMap.put("B", 3);
//      hashBiMap.putIfAbsent("C", 2);

        //双键Map
        HashBasedTable<String, String, Object> basedTable = HashBasedTable.create();
        basedTable.put("A", "B", 1);
        basedTable.put("A", "C", 2);
        Object o = basedTable.get("A", "C");
        System.out.println("双键Map" + o);

        //Joiner
        //a-b-c
        List<String> list = Lists.newArrayList("a", "b", "c");
        String join = Joiner.on("-").join(list);
        System.out.println("Joiner-List :" + join);

        //用C 代替null ：=C,C=2,B=1
        String join1 = Joiner.on(",").withKeyValueSeparator("=").useForNull("C").join(map);
        System.out.println("Joiner-Map :" + join1);

        String str = "1,null,2,,3,\"\",4, 5,     6";
        //去除空串、空格 Map转固定格式字符串
        List<String> list1 = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(str);
        System.out.println("Splitter:" + list1);

        //固定格式字符串转map
        UserTest userTest = new UserTest("AA", 10);
        String s = userTest.toString();
        String remove = StringUtils.remove(s, UserTest.class.getName());
//        String s1 = StringUtils.replaceEach(remove, new String[]{"(", ")"}, new String[]{"",""});
//        String s1 = StringUtils.replace(remove, "(", "").replace(")", "");
        String s1 = StringUtils.replaceAll(remove, "[()]", "");
        Map<String, String> split = Splitter.on(",").trimResults().withKeyValueSeparator("=").split(s1);
        split.forEach((k, v) -> System.out.println(k + "--" + v));

        Function<Integer, Integer> function = f -> {
            if (Objects.nonNull(f)) {
                return f += 1;
            }
            return f;
        };

        Function<Integer, Double> sqrt =
                new Function<Integer, Double>() {
                    public Double apply(Integer in) {
                        return Math.sqrt((int) in);
                    }
                };
        Map<String, Integer> map1 = ImmutableMap.of("B", 1, "C", null);
//        Maps.transformValues(map1, sqrt);
    }

    @Test
    public void stringRemoveOrReplace() {
        String str = "(,1,2,3,4,(,),8,[,9,]";
        //同时排除[] ()
        String s = StringUtils.removeAll(str, "[\\[\\]()]");
        System.out.println(s);

    }

    @Test
    public void optional() {
//        List<String> list = Lists.newArrayList();
        List<String> list = null;
        Optional<String> s1 = Optional.ofNullable(list).map(s -> s + ",");
        System.out.println(s1);

    }

    @Test
    public void guava_Iterables() {

        //Iterables / Predicates
        //过滤
        List<String> list = Lists.newArrayList("A", "B", "C");
        Iterable<String> b = Iterables.filter(list, Predicates.equalTo("B"));
        ArrayList<String> strings = Lists.newArrayList(b);
        System.out.println("1       " + strings);

        //数据转换
        com.google.common.base.Function<String, String> function = s -> s.toLowerCase();
        Iterable<String> transform = Iterables.transform(list, function);
        System.out.println("2       " + transform);

        //any操作包含
        boolean a = Iterables.any(list, Predicates.equalTo("a"));
        boolean d = Iterables.contains(list, "D");
        System.out.println("3       " + a + d);


        //合并
        List<String> list_2 = Lists.newArrayList("A", "B", "C");
        Iterable<String> concat = Iterables.concat(list, list_2);
        System.out.println("4       " + concat);

        //两个集合元素完全相同 - true
        boolean b1 = Iterables.elementsEqual(list, list_2);
        System.out.println("5       " + b1);

        //集合元素出现频率
        int frequency = Iterables.frequency(list, "A");
        System.out.println("6       " + frequency);

       /* String onlyElement = Iterables.getOnlyElement(list);
        System.out.println("7       "+onlyElement);*/

        //获取集合第一个/最后一个元素
        Iterables.getFirst(list, 1);
        Iterables.getLast(list);

        List<UserTest> userTests = Lists.newArrayList(new UserTest(new Date()), new UserTest(new Date(1527926658874L)));
    }

    @Test
    public void guava_Sets_enumUtils() {
        Set set_1 = Sets.newHashSet(1, 2, 3, 4);
        Set set_2 = Sets.newHashSet(3, 4, 5, 6);

        //前者相对后者的不同元素
        Sets.SetView difference = Sets.difference(set_2, set_1);
        System.out.println(difference);//[5,6]

        //合并set
        Sets.SetView union = Sets.union(set_1, set_2);
        System.out.println(union);//[1, 2, 3, 4, 5, 6]

        //两个set 的交叉元素
        Sets.SetView intersection = Sets.intersection(set_1, set_2);
        System.out.println(intersection);//[3, 4]

        PushType anEnum = EnumUtils.getEnum(PushType.class, "NEW_HELLO");

        //枚举code - value 生成Map
        List<PushType> enumList = EnumUtils.getEnumList(PushType.class);
        Map<Integer, String> enumMap = enumList.stream().collect(Collectors.toMap(PushType::getCode, PushType::getValue));

        //key: NAME值
        Map<String, PushType> enumMap1 = EnumUtils.getEnumMap(PushType.class);
    }

    @Test
    public void timestamp() {
        long time = 1527858072;
        Date date = new Date(time * 1000); //秒级 * 1000
        System.out.println(FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").format(date));
    }

    @Test
    public void guava_MoreObjects() {
        UserTest userTest = new UserTest();
        userTest.setName("Taylor");
        System.out.println(userTest);
        String name = MoreObjects.toStringHelper(UserTest.class).add("name", userTest.getName()).toString();
        System.out.println(name);
    }

    @Test
    public void guava_order() {
        Ordering<Comparable> comparableOrdering = Ordering.natural().nullsLast();
    }

    //代码运行时常
    @Test
    public void guava_stopWatch() {
        Stopwatch started = Stopwatch.createStarted();
        for (int i = 0; i < 1000000; i++) {

        }
        long elapsed = started.elapsed(TimeUnit.MILLISECONDS);
        System.out.println("耗时：" + elapsed + "ms");
    }

    //guava 缓存策略
    @Test
    public void guava_cache() throws ExecutionException {
        Cache<Object, Object> build = CacheBuilder.newBuilder().build();
        build.put("A", "Taylor");
        Object b = build.getIfPresent("A");

        LoadingCache<String, String> build_1 = CacheBuilder.newBuilder().build(new CacheLoader<String, String>() {
            @Override
            public String load(String key) throws Exception {
                return "Hello" + key;
            }
        });
        String s = build_1.get("B");
        System.out.println(s); // HelloB

        LoadingCache<String, String> build_2 = CacheBuilder.newBuilder()
                .maximumSize(100)
                //过期时间
                .expireAfterWrite(20, TimeUnit.SECONDS)
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String key) throws Exception {
                        System.out.println();
                        return "Hi" + key;
                    }
                });
        build_2.put("D", "DDDDDD");
        String c = build_2.get("C");
        String d = build_2.get("D");
        System.out.println(c + "---" + d);

    }


    // mysql获取随机： select id,nickname from club_user where id >= ((select MAX(id) from club_user)- (select min(id) from club_user)) * rand() + (select min(id) from club_user) limit 100;
    @Test
    public void guava_ComparisonChain() {
        //链式比较
        //第一个compare相等的情况下进行后续compare比较否则以第一个compare结果为准
        int result = ComparisonChain.start().compare(2, 2).compare(2, 3).result();
        System.out.println(result);//-1
    }

    @Test
    public void guava_base() {
        //Ints
        List<Integer> integers = Ints.asList(1, 2, 3, 0, 10, 21, 11);
        Ints.max(1, 2, 4, 3, 19, 0, 2);

        //获取集合最大值
        Integer max1 = Collections.max(integers);
        Integer max = Ordering.natural().max(integers);
        System.out.println(max + "===============" + max1);

        //获取最大2个数据
        List<Integer> integers1 = Ordering.natural().greatestOf(integers, 2);
        System.out.println(integers1);

        String str = "";
        System.out.println(Strings.emptyToNull(str));
        System.out.println(Strings.isNullOrEmpty(str));

        //初始化时指定容器大小
        ArrayList<Object> objects = Lists.newArrayListWithCapacity(5);

        //区间操作
        boolean contains = Range.closed(1, 4).intersection(Range.closed(2, 5)).contains(3);
        System.out.println(contains);


    }

    @Test
    public void guava_future() {



    }

}
