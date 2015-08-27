package torrent.queue;

import java.net.InetSocketAddress;

public interface TaskBuilder {
    PeerAdditionRequest getPeerAdditionRequest(InetSocketAddress address);

    PeerDeletionRequest getPeerDeletionRequest(long id, String reason);

    PeerRegistrationRequest getPeerRegistrationRequest(long id, byte[] name);

    PeerCreationRequest getPeerCreationRequest(long id);

    MessageSendRequest getMessageSendRequest(long id, byte[] message);
}
