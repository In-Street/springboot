package cyf.gradle.interview.service.NIO;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author Cheng Yufei
 * @create 2019-04-14 16:46
 **/
public class SocketClient {

    public static void main(String[] args) throws IOException {
        new Thread(() -> {
            Socket socket = new Socket();
            InetSocketAddress address = new InetSocketAddress("127.0.0.1", 1234);
            try {
                socket.connect(address);

                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream();

                PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(outputStream));
                //没有\n 发布出去
                printWriter.write("hello server\n");
                printWriter.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String readString = reader.readLine();
                System.out.println("服务端发来消息：" + readString);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();


    }
}
