package torrent.queue;

import java.net.InetAddress;

public interface TaskBuilder {
    PeerAdditionRequest getPeerAdditionRequest(InetAddress address);

    PeerDeletionRequest getPeerDeletionRequest(long id, String reason);


}
