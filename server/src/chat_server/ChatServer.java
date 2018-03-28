package chat_server;

import chat_network.TCP;
import chat_network.TCPCONNECTIONLISTENME;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ChatServer implements TCPCONNECTIONLISTENME {
    public static void main(String[] args) {
        new ChatServer();
    }

    ArrayList<TCP> connections = new ArrayList<>();

    private ChatServer() {
        System.out.println("Server running...");
        try (ServerSocket serverSocket = new ServerSocket(101)) {
            while (true) {
                try {
                    new TCP(this, serverSocket.accept());
                } catch (IOException e) {
                    System.out.println("TCPconncetion exception:" + e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void OnConnectionReady(TCP tcp) {
        connections.add(tcp);
        sendToAllconnections("Client connected:" + tcp);
    }

    @Override
    public synchronized void OnReceiveString(TCP tcp, String value) {
        sendToAllconnections(value);
    }

    @Override
    public synchronized void onDisconect(TCP tcp) {
        connections.remove(tcp);
        sendToAllconnections("Client disconnected:" + tcp);
    }

    @Override
    public synchronized void Execute(TCP tcp, Exception e) {
        System.out.println("TCPconnection exeption:" + e);
    }

    private void sendToAllconnections(String value) {
        System.out.println(value);
        for (int i = 0; i < connections.size(); i++) {
            connections.get(i).SentString(value);
        }
    }
}
