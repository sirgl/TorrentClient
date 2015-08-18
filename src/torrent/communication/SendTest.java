package torrent.communication;

import java.io.IOException;
import java.net.InetSocketAddress;

public class SendTest {
    public static void main(String[] args) throws IOException {
        CommunicationService communicationService = new CommunicationService();
        PeerManagerImpl peerManager = new PeerManagerImpl();
        ConnectionHandler connectionHandler = new ConnectionHandler(new InetSocketAddress(4444), peerManager, communicationService);
        new Thread(connectionHandler).start();

    }
}
