package torrent.queue;

import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueHandler implements QueueHandlerInterface, Runnable {
    ConcurrentLinkedQueue<Runnable> tasks = new ConcurrentLinkedQueue<>();

    @Override
    public void addTask(Runnable task) {
        tasks.add(task);
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            Runnable task = tasks.remove();
            task.run();
        }
    }
}