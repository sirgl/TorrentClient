package torrent.queue;

import torrent.communication.PeerManager;

import java.nio.ByteBuffer;

public class PeerRegistrationRequest implements Runnable {
    private final long id;
    private final byte[] name;
    private final PeerManager peerManager;

    public PeerRegistrationRequest(long id, byte[] name, PeerManager peerManager) {
        this.id = id;
        this.name = name;
        this.peerManager = peerManager;
    }

    @Override
    public void run() {
        peerManager.registerPeer(id, name);

    }
}
