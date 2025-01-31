package pl.pwr.ite.dynak.services;

import pl.pwr.ite.dynak.utils.InvalidMethodException;
import pl.pwr.ite.dynak.utils.Method;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class SocketUser {
    protected ServerSocket serverSocket;
    int port;
    protected ExecutorService executor;
    protected SocketUser(int port) {
        this.port = port;
        this.executor = Executors.newCachedThreadPool();
    }
    public void startListening() throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server listening on port " + port);
        while (!Thread.currentThread().isInterrupted()) {
            Socket socket = serverSocket.accept();
            executor.submit(() -> respondToRequest(socket));
        }
    }
    private void respondToRequest(Socket socket) {
        try (BufferedReader inbound = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter outbound = new PrintWriter(socket.getOutputStream(), true)) {
            String rawRequest = inbound.readLine();
            Method request = parseRequest(rawRequest);
            if (request != null) {
                int response = handleRequest(request);
                outbound.println(response);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private Method parseRequest(String request) {
        if (request != null) {
            String[] parts = request.split(":");
            String methodName = parts[0];
            String params = parts[1];
            String[] methodParams = params.split(",");
            return switch (methodName) {
                case "sj", "r", "o" -> new Method(methodName, methodParams[0], methodParams[1], null);
                case "spi" -> new Method(methodName, null, methodParams[0], methodParams[1]);
                case "gp", "gs", "sr", "spo" -> new Method(methodName, null, methodParams[0], null);
                default -> null;
            };
        }
        return null;
    }
    protected String sendRequest(String request, String host, int port){
        try (Socket socket = new Socket(host, port);
             PrintWriter outbound = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader inbound = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             ) {
            socket.setSoTimeout(5000);
            outbound.println(request);
            return inbound.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    protected abstract int handleRequest(Method method) throws InvalidMethodException;
}
