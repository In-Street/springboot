import com.google.common.base.Stopwatch;
import com.google.common.io.Files;
import org.junit.Test;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Cheng Yufei
 * @create 2019-03-16 17:53
 **/
public class IO {
    @Test
    public void stream() throws IOException {
        FileInputStream inputStream = new FileInputStream(new File("/Users/chengyufei/Downloads/a.txt"));
        FileOutputStream outputStream = new FileOutputStream(new File("/Users/chengyufei/Downloads/b.txt"), true);
        byte[] bytes = new byte[128];
        while (inputStream.read(bytes) != -1) {
            outputStream.write(bytes);
        }
    }

    /**
     * NIO transferTo 零拷贝复制文件【内核和磁盘数据传输，无需经过用户空间】 4ms
     *
     * FileCopyUtils.copy(new File("D:/A.txt"), new File("D:/C.txt")); 最快16ms
     *
     * @throws IOException
     */
    @Test
    public void transTo() throws IOException {

        Stopwatch started = Stopwatch.createStarted();
        FileInputStream fileInputStream = new FileInputStream(new File("D:/A.txt"));
        FileChannel inChannel = fileInputStream.getChannel();
        FileChannel outChannel = new FileOutputStream(new File("D:/C.txt")).getChannel();

        for (long size = inChannel.size(); size > 0; ) {
            long transferNum = inChannel.transferTo(inChannel.position(), size, outChannel);
            inChannel.position(inChannel.position() + transferNum);
            size = size - transferNum;
        }
        System.out.println(started.elapsed(TimeUnit.MILLISECONDS));

    }

}
