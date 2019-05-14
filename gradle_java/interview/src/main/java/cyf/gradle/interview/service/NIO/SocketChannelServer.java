package cyf.gradle.interview.service.NIO;

import lombok.SneakyThrows;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Cheng Yufei
 * @create 2019-05-14 15:33
 **/
public class SocketChannelServer {

    private static Selector selector;
    private Charset charset = Charset.forName("utf-8");

    static {
        try {
            selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(6666));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        SocketChannelServer channelServer = new SocketChannelServer();
        channelServer.listen();
    }

    @SneakyThrows
    private void listen() {
        while (selector.select() > 0) {
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();

                if (key.isAcceptable()) {
                    accept(key);
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
    private void accept(SelectionKey key) {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverSocketChannel.accept();
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ);

        InetAddress inetAddress = channel.socket().getInetAddress();
        System.out.println(inetAddress.getHostName()+"|"+inetAddress.getAddress()+"连接成功");
    }

    @SneakyThrows
    private void read(SelectionKey key) {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        channel.read(buffer);
        buffer.flip();
        String receiveData = buffer.toString();
        System.out.println("服务端接受数据：" + charset.decode(buffer).toString());

        if ("".equals(receiveData)) {
            key.cancel();
            channel.close();
        }
    }

    @SneakyThrows
    private void write(SelectionKey key) {
        SocketChannel channel = (SocketChannel) key.channel();
        channel.write(charset.encode("服务端发送数据"));
        key.interestOps(SelectionKey.OP_READ);
    }

}
