package torrent.communication;

import java.io.IOException;
import java.net.InetSocketAddress;

public interface CommunicationController {
    void addNewPeer(InetSocketAddress address) throws IOException;

    void deletePeer(long peerId);

}
