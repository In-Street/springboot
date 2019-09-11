package cyf.gradle.interview.configuration;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Cheng Yufei
 * @create 2019-09-08 1:36 下午
 **/
@Slf4j
public class MyRequestInputStream extends HttpServletRequestWrapper {

    private final byte[] bytes;

    public MyRequestInputStream(HttpServletRequest request) throws IOException {
        super(request);

        InputStream inputStream = request.getInputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] by = new byte[4096];

        int length;
        while ((length = inputStream.read(by)) != -1) {
            outputStream.write(by, 0, length);
        }
        bytes = outputStream.toByteArray();
        log.info(">>自定义RequestInputStream：bytes size:{}", bytes.length);
    }

    /**
     * 重写 getInputStream ，之后每次获取流都会重新赋值，达到多次使用流的目的
     * @return
     * @throws IOException
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {

            }
        };
    }
}
