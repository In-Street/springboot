package cyf.gradle.interview.service.NIO;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Cheng Yufei
 * @create 2019-04-14 17:08
 **/
public class SocketServer {
    public static void main(String[] args)  {

        new Thread(()->{
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(1234);
                while (true) {
                    Socket acceptSocket = serverSocket.accept();
                    System.out.println("客户端：" + acceptSocket.getInetAddress()+ "已连接服务器");

                    BufferedReader reader = new BufferedReader(new InputStreamReader(acceptSocket.getInputStream()));
                    String readResult = reader.readLine();
                    System.out.println("客户端发来消息：" + readResult);

                    PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(acceptSocket.getOutputStream()));
                    printWriter.write("hello client\n");
                    printWriter.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();

    }
}
