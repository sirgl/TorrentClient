package torrent.communication;

public class PeerDeletionTask implements Runnable {
    private final long id;
    private final ConnectionHandler connectionHandler;

    public PeerDeletionTask(long id, ConnectionHandler connectionHandler) {
        this.id = id;
        this.connectionHandler = connectionHandler;
    }

    @Override
    public void run() {
        connectionHandler.removePeer(id);
        connectionHandler.removePipelines(id);
    }
}
