package torrent.queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class QueueHandlerImpl implements QueueHandler, Runnable {
    public static final int TASK_QUEUE_CAPACITY = 1000;
    BlockingQueue<Runnable> tasks = new ArrayBlockingQueue<>(TASK_QUEUE_CAPACITY);

    @Override
    public void addTask(Runnable task) {
        tasks.add(task);
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                Runnable task = tasks.take();
                task.run();
                System.out.println("executed! " + task);
            }
        } catch (InterruptedException e) {
            //TODO log
            System.out.println("Interrupted!");
        }
    }
}
