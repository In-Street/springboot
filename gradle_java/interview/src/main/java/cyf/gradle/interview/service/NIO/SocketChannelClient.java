package cyf.gradle.interview.service.NIO;

import lombok.SneakyThrows;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Cheng Yufei
 * @create 2019-05-14 15:32
 **/
public class SocketChannelClient {
    private static Selector selector;
    private Charset charset = Charset.forName("utf-8");

    static {
        try {
            selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public static void main(String[] args) {

        SocketChannelClient client = new SocketChannelClient();
        client.request(6666);
        client.request(7777);
        client.listen();
    }

    @SneakyThrows
    private void request(int port) {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress("127.0.0.1", port));
        socketChannel.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ | SelectionKey.OP_WRITE);
    }

    private void listen() throws IOException {
        while (selector.select() > 0) {
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();

                if (key.isConnectable()) {
                    connect(key);
                }
                if (key.isReadable()) {
                    read(key);
                }
                if (key.isWritable()) {
                    write(key);
                }
                iterator.remove();
            }
        }
    }

    @SneakyThrows
    private void connect(SelectionKey key) {
        SocketChannel channel = (SocketChannel) key.channel();
        channel.finishConnect();
        InetAddress inetAddress = channel.socket().getInetAddress();
        System.out.println("请求连接：" + inetAddress.getHostName() + inetAddress.getAddress());
    }

    @SneakyThrows
    private void read(SelectionKey key) {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        channel.read(buffer);
        buffer.flip();
        String receiveData = buffer.toString();
        System.out.println("客户端接受数据：" + charset.decode(buffer).toString());

        if ("".equals(receiveData)) {
            key.cancel();
            channel.close();
        }
    }

    @SneakyThrows
    private void write(SelectionKey key) {
        SocketChannel channel = (SocketChannel) key.channel();
        channel.write(charset.encode("客户端发送数据"));
        key.interestOps(SelectionKey.OP_READ);
    }
}
