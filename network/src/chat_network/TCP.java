package chat_network;

import java.io.*;
import java.net.Socket;
import java.nio.Buffer;
import java.nio.charset.Charset;

public class TCP {

    private final Socket socket;
    private final Thread rxThread;
    private final BufferedReader in;
    private final BufferedWriter out;
    private final TCPCONNECTIONLISTENME eventListener;

    public  TCP(TCPCONNECTIONLISTENME eventListener, String ipAdress, int port) throws IOException{
        this(eventListener, new Socket(ipAdress,port));
    }

    public TCP(TCPCONNECTIONLISTENME eventListener, Socket socket) throws java.io.IOException {
        this.eventListener = eventListener;
        this.socket = socket;
        socket.getInputStream();
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")));
        rxThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("MACHINA");
                    eventListener.OnConnectionReady(TCP.this);
                    while (!rxThread.isInterrupted()) {
                        String msg = in.readLine();
                        eventListener.OnReceiveString(TCP.this, msg);
                    }
                } catch (IOException e) {
                    eventListener.Execute(TCP.this, e);
                } finally {
                    eventListener.onDisconect(TCP.this);
                }
            }
        });
        rxThread.start();
    }

    public synchronized void SentString(String value) {
        try {
            out.write(value + "\r\n");
            out.flush();
        } catch (IOException e) {
            eventListener.Execute(TCP.this, e);
            disconect();
        }
    }

    public synchronized void disconect() {
        rxThread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            eventListener.Execute(TCP.this, e);
        }
    }

    @Override
    public String toString() {
        return "TCPconnection" + socket.getInetAddress() + ":" + socket.getPort();
    }
}
