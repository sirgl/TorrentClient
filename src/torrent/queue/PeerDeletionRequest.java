package torrent.queue;

public class PeerDeletionRequest implements Runnable {

    private final String reason;

    PeerDeletionRequest(String reason) {
        this.reason = reason;
    }

    @Override
    public void run() {
        System.err.println("deleting peer");
        System.err.println(reason);
    }
}
