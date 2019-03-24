import org.junit.Test;

import java.io.*;

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
}
