import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.*;
import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.EmojiParser;
import cyf.gradle.base.enums.PraiseEnum;
import cyf.gradle.util.EmojiRegexUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Cheng Yufei
 * @create 2018-05-18 18:49
 **/
public class TestCompany_xclub {

    @Test
    public void t1() {
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
//        System.out.println(collect);

        List<Integer> list1 = Lists.newArrayList(1, 3, null);
        List<Integer> uids = list1.parallelStream().distinct().collect(Collectors.toList());
        System.out.println(uids);

    }

    @Test
    public void t2() {
        List list = Lists.newArrayList(1, 3, 4, 5, 6, 7, 8, 9, 10, 2);
        List list1 = Lists.newArrayList(1, 3, 4, 20, 21);
        list.removeAll(list1);
        System.out.println(list);
    }

    @Test
    public void t3() {
       /* ClubChatGroup g1 = new ClubChatGroup();
        g1.setId(1);
        g1.setFromUid(3);
        g1.setToUid(2);

        ClubChatGroup g2 = new ClubChatGroup();
        g2.setId(2);
        g2.setFromUid(3);
        g2.setToUid(4);

        ClubChatGroup g3 = new ClubChatGroup();
        g3.setId(3);
        g3.setFromUid(5);
        g3.setToUid(3);
        g3.setType(3);

        List<ClubChatGroup> groups = Lists.newArrayList();
        groups.add(g1);
        groups.add(g2);
        groups.add(g3);

        Integer finalUid = 3;
        Function<ClubChatGroup, Integer> function = c -> {

            if (Objects.equals(finalUid, c.getFromUid())) {
                return c.getToUid();
            } else {
                return c.getFromUid();
            }
        };
        Map<Integer, List<ClubChatGroup>> map = groups.parallelStream().collect(Collectors.groupingBy(function));

        List<Integer> list = groups.parallelStream().map(function).collect(Collectors.toList());


//        map.forEach((k, v) -> System.out.println(k + "---->" + v.get(0).getId()));

//        System.out.println(list);

        Predicate<ClubChatGroup> p = g -> Objects.equals(ChatGroupType.MATCHED.getCode(), g.getType());
        boolean b = groups.parallelStream().noneMatch(p);
//        System.out.println(b);

        Map map1 = Maps.newHashMap();
        map1.put(1, "sssss");
        Function<ClubChatGroup, ClubChatGroup> function1 = c -> {
            if (!map1.containsKey(c.getId())) {
                return null;
            }
            return c;
        };
        List<ClubChatGroup> collect = groups.parallelStream().map(function1).filter(s -> Objects.nonNull(s)).collect(Collectors.toList());
        System.out.println(collect);
*/
    }

    @Test
    public void t4() {
        //language=JSON
        String str = "{\"id\":1,\"name\":\"str\"}";
        System.out.println(str);
    }

    @Test
    public void t5() throws ParseException {
        Integer uid = 2;
        if (uid != 0 && uid != -1) {
            System.out.println("aaaaa");
        }
    }

    @Test
    public void t6() {

        String str = "abc\uD83D\uDE1C\uD83D\uDE29\uD83D\uDE29";
        String[] strings = StringUtils.splitByCharacterType(str);
        for (String s : strings) {
//            System.out.println(s + "---");
        }

        char[] chars = str.toCharArray();
        for (char c : chars) {
            System.out.println(c);
        }
    }

    @Test
    public void t7() {

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
    public void t8() throws Exception {
        PraiseEnum[] values = PraiseEnum.values();
        List<PraiseEnum> noChatRemoveMatchReasons = Arrays.asList(values);

        Function<PraiseEnum, RemoveMatchReason> function = s -> new RemoveMatchReason(s.getCode(), s.getName());
        List<RemoveMatchReason> collect = Arrays.stream(values).map(function).collect(Collectors.toList());

        System.out.println(collect);
    }

    @Test
    public void t9() {
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
        //likeDto为null时返回一个Optional空对象
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
        map.put("aa", new LikeDto());
        Optional.ofNullable(map.get("aa")).ifPresent(s -> s.setAvatar("http://www.xxxxxx"));
        System.out.println("5---" + map.get("aa"));

        //Stream接口里带静态方法,用于创建Stream
        Stream<String> stringStream = Stream.of("a,b");
        Stream.empty();
        String[] strings = new String[]{"cyf","taylor"};
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
    public void t10() {
//        LikeDto likeDto = null;
        LikeDto likeDto = new LikeDto();
       likeDto.setNickname("Taylor");
        LikeDto dto = Preconditions.checkNotNull(likeDto, "参数错误");
        System.out.println(dto);

        Joiner.on("a");

        String[] strings = new String[]{"a","b","c"};
        Stream<String> stream = ImmutableList.copyOf(strings).stream();
        ImmutableList.of(strings);


//        Multiset<String> set = LinkedHashMultiset.create();
        Multiset<String> set =HashMultiset.create();
        set.add("a");
        set.add("a");
        set.add("b");
        int a = set.count("a");
        System.out.println(a);


    }
}
