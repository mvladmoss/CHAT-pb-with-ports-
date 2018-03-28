package chat_network;

public interface TCPCONNECTIONLISTENME {
    void OnConnectionReady(TCP tcp);
    void OnReceiveString(TCP tcp, String value);
    void onDisconect(TCP tcp);
    void Execute(TCP tcp, Exception e);
}
