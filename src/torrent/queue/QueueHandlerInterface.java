package torrent.queue;

public interface QueueHandlerInterface {
    void addTask(Runnable task);
}
