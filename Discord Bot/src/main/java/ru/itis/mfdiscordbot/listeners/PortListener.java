package ru.itis.mfdiscordbot.listeners;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class PortListener {
    private ServerSocket serverSocket;
    private List<ClientSocket> connections;

    public PortListener(short port) {
        try {
            serverSocket = new ServerSocket(port);
            connections = new ArrayList<>();
        } catch (IOException e) {
            //TODO
        }
    }

    public void start() {
        while (true) {
            try {
                ClientSocket clientSocket = new ClientSocket(serverSocket.accept());
                clientSocket.start();
                connections.add(clientSocket);
            } catch (IOException e) {

            }
        }
    }


    private static class ClientSocket extends Thread {
        private Socket clientSocket;


        private ClientSocket(Socket socket) {
            clientSocket = socket;
        }
    }

}
