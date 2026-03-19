package com.example.rpc.example.nativeio;

import com.example.rpc.example.api.HelloService;
import com.example.rpc.example.provider.HelloServiceImpl;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 原生 Socket 服务端示例。
 * 只使用 JDK 自带的 ServerSocket 和对象流完成一次最小版 RPC 调用。
 */
public class SocketDemoServer {

    private static final int PORT = 9090;
    private final Map<String, Object> serviceMap = new HashMap<String, Object>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * 程序入口。
     *
     * @param args 命令行参数
     * @throws Exception 启动异常
     */
    public static void main(String[] args) throws Exception {
        SocketDemoServer server = new SocketDemoServer();
        server.registerServices();
        server.start();
    }

    /**
     * 注册本地可调用的服务实现。
     */
    private void registerServices() {
        serviceMap.put(HelloService.class.getName(), new HelloServiceImpl());
    }

    /**
     * 启动原生 Socket 服务端并循环接收请求。
     *
     * @throws Exception 启动或通信异常
     */
    private void start() throws Exception {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Native socket server started on port " + PORT);
        while (true) {
            final Socket socket = serverSocket.accept();
            executorService.submit(new Runnable() {
                public void run() {
                    handleClient(socket);
                }
            });
        }
    }

    /**
     * 处理一个客户端连接。
     *
     * @param socket 客户端连接
     */
    private void handleClient(Socket socket) {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());

            SocketRpcRequest request = (SocketRpcRequest) inputStream.readObject();
            Object result = invoke(request);

            outputStream.writeObject(SocketRpcResponse.success(result));
            outputStream.flush();
        } catch (Exception e) {
            try {
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(SocketRpcResponse.fail(e.getMessage()));
                outputStream.flush();
            } catch (Exception ignored) {
            }
        } finally {
            try {
                socket.close();
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * 根据请求信息通过反射调用真实服务方法。
     *
     * @param request 请求对象
     * @return 方法返回值
     * @throws Exception 反射调用异常
     */
    private Object invoke(SocketRpcRequest request) throws Exception {
        Object service = serviceMap.get(request.getServiceName());
        if (service == null) {
            throw new IllegalStateException("Service not found: " + request.getServiceName());
        }
        Method method = service.getClass().getMethod(request.getMethodName(), request.getParameterTypes());
        return method.invoke(service, request.getParameters());
    }
}
