package ru.itis.mfbotsapi.api;

import ru.itis.mfbotsapi.api.client.Client;
import ru.itis.mfbotsapi.api.client.ManagementClient;
import ru.itis.mfbotsapi.api.protocol.TCPFrameFactoryImpl;
import ru.itis.mfbotsapi.api.utils.ManagementClientKeyManager;

import java.net.InetSocketAddress;

public class TestClient {

    public static void main(String[] args) throws Throwable {
        Client client = ManagementClient.init(new ManagementClientKeyManager(),
                new TCPFrameFactoryImpl((byte) 0XCC, (byte) 0xDD, 2048, 64, 0),
                null
        );
        Runnable clientRun = () -> {
            InetSocketAddress serverTCPAddress = new InetSocketAddress("localhost", 5467);
            client.connect(serverTCPAddress, "key");
        };
        Thread clientThread = new Thread(clientRun);
        clientThread.setDaemon(true);
        clientThread.start();
        try{
            Thread.sleep(5000); //Time to server settings
        } catch (InterruptedException e) {
            //ignore
        }
    }

}
