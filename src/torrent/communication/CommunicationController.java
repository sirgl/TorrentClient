package torrent.communication;

import java.net.InetAddress;

public interface CommunicationController {
    void addNewPeer(InetAddress address);
    void deletePeer(long peerId);
}
