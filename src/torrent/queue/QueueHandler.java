package torrent.queue;

public interface QueueHandler {
    void addTask(Runnable task);
}
