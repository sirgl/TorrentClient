package torrent.queue;

import torrent.communication.PeerManager;

/**
 * Request to create peer from connection handler
 */
public class PeerCreationRequest implements Runnable {
    private final long id;
    private final PeerManager manager;

    public PeerCreationRequest(long id, PeerManager manager) {
        this.id = id;
        this.manager = manager;
    }

    @Override
    public void run() {
        manager.addPeer(id);
    }
}
