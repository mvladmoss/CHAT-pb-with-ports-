package chat;

import chat_network.TCP;
import chat_network.TCPCONNECTIONLISTENME;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ClientWindow extends JFrame implements ActionListener, TCPCONNECTIONLISTENME{

    private static final String IP = "172.16.1.103";
    private static final int Port = 101;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientWindow();
            }
        });
    }

    private final JTextArea log = new JTextArea();
    private final JTextField NickName = new JTextField("Vlad");
    private final JTextField FieldInput = new JTextField();

    private TCP connection;

    private ClientWindow() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH,HEIGHT);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        log.setEditable(false);
        log.setLineWrap(true);
        FieldInput.addActionListener(this);
        add(log, BorderLayout.CENTER);
        add(NickName, BorderLayout.NORTH);
        add(FieldInput, BorderLayout.SOUTH);
        setVisible(true);
        try {
            connection = new TCP(this, IP, Port);
        } catch (IOException e) {
            System.out.println("Connection exception" + e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = FieldInput.getText();
        if(msg.equals(""))
            return;
        FieldInput.setText(null);
        connection.SentString(NickName.getText() + ":" + msg);
    }

    @Override
    public void OnConnectionReady(TCP tcp) {
        System.out.println("Connection ready...");
    }

    @Override
    public void OnReceiveString(TCP tcp, String value) {
        printMsg(value);
    }

    @Override
    public void onDisconect(TCP tcp) {
        System.out.println("Connection close...");
    }

    @Override
    public void Execute(TCP tcp, Exception e) {
        System.out.println("Connection exception" + e);
    }
    private synchronized void printMsg(String Msg){
SwingUtilities.invokeLater(new Runnable() {
    @Override
    public void run() {
        log.append(Msg + "\n");
        log.setCaretPosition(log.getDocument().getLength());
    }
});
    }
}
