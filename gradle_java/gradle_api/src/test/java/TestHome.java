import org.apache.shiro.crypto.hash.Md5Hash;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

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
        List<String> list = Lists.newArrayList("cheng","yu","fei","yu");
        System.out.println(list);
        //排除重复数据
        List<String> collect = list.stream().distinct().collect(Collectors.toList());
        System.out.println(collect);

        List<String> collect1 = list.stream().limit(2).collect(Collectors.toList());
        System.out.println(collect1);

        //skip(n): 返回一个忽略前n个的stream
        List<String> collect2 = list.stream().skip(2).collect(Collectors.toList());
        System.out.println(collect2);


        //文件转 Stream
        List<String> collect3 = Files.lines(Paths.get("/Users/chengyufei/Downloads/b.txt"), Charset.defaultCharset()).collect(Collectors.toList());
        System.out.println(collect3);

    }
}
