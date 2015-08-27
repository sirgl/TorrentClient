package torrent.queue;

import torrent.communication.CommunicationController;
import torrent.communication.PeerManager;

public class PeerDeletionRequest implements Runnable {
    private final CommunicationController communicationController;
    private final String reason;
    private final long id;
    private final PeerManager manager;

    PeerDeletionRequest(CommunicationController communicationController, String reason, long id, PeerManager manager) {
        this.communicationController = communicationController;
        this.reason = reason;
        this.id = id;
        this.manager = manager;
    }

    @Override
    public void run() {
        manager.removePeer(id);
        communicationController.deletePeer(id);


        System.err.println("deleting peer");
        System.err.println(reason);
    }
}
