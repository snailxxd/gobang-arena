package com.example.app;

import com.example.service.server.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static com.example.utils.Utils.*;

public class PlatformServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Listening on: " + serverSocket.getInetAddress() + ":" + serverSocket.getLocalPort());
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected" + socket.getInetAddress() + ":" + socket.getPort());
                new Thread(new ClientHandler(socket)).start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + SERVER_PORT);
        }
    }
}
