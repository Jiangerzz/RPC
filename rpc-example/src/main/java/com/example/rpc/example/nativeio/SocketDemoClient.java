package com.example.rpc.example.nativeio;

import com.example.rpc.example.api.HelloService;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * 原生 Socket 客户端示例。
 * 直接通过 JDK Socket 连接服务端，发送请求对象并读取响应对象。
 */
public class SocketDemoClient {

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 9090;

    /**
     * 程序入口。
     *
     * @param args 命令行参数
     * @throws Exception 通信异常
     */
    public static void main(String[] args) throws Exception {
        SocketRpcRequest request = new SocketRpcRequest();
        request.setServiceName(HelloService.class.getName());
        request.setMethodName("sayHello");
        request.setParameterTypes(new Class<?>[]{String.class});
        request.setParameters(new Object[]{"Native Socket"});

        Socket socket = new Socket(HOST, PORT);
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

        outputStream.writeObject(request);
        outputStream.flush();

        SocketRpcResponse response = (SocketRpcResponse) inputStream.readObject();
        if (response.isSuccess()) {
            System.out.println("Native socket call success: " + response.getData());
        } else {
            System.out.println("Native socket call failed: " + response.getMessage());
        }

        socket.close();
    }
}
