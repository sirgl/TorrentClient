package torrent.communication;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class CommunicationService implements CommunicationController {
    private final ConnectionHandler connectionHandler;

    public CommunicationService(ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
    }


    /**
     * To avoid deadlock it must be used only by queue handler thread
     */
    @Override
    public void addNewPeer(InetSocketAddress address) throws IOException {
        connectionHandler.connectToPeer(address);
    }

    /**
     * To avoid deadlock it must be used only by queue handler thread
     */
    @Override
    public void deletePeer(long peerId) {
        connectionHandler.addTask(new PeerDeletionTask(peerId, connectionHandler));
    }


}
