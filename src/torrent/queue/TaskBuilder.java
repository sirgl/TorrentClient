package torrent.queue;

import java.net.InetAddress;
import java.nio.ByteBuffer;

public interface TaskBuilder {
    PeerAdditionRequest getPeerAdditionRequest(InetAddress address);

    PeerDeletionRequest getPeerDeletionRequest(long id, String reason);

    PeerRegistrationRequest getPeerRegistrationRequest(long id, ByteBuffer name);


}
