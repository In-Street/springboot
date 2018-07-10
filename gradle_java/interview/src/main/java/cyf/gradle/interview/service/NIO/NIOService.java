package cyf.gradle.interview.service.NIO;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

/**
 * @author Cheng Yufei
 * @create 2018-07-10 17:33
 **/
@Slf4j
public class NIOService {

    public static void client() throws IOException {
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1",7070));
//        socketChannel.configureBlocking();

    }
}
