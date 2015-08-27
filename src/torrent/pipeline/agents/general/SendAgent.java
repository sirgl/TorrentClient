package torrent.pipeline.agents.general;

import torrent.communication.ConnectionHandler;
import torrent.pipeline.PipelineContext;
import torrent.pipeline.agents.Agent;
import torrent.queue.QueueHandler;
import torrent.queue.TaskBuilder;

import java.io.IOException;
import java.nio.ByteBuffer;

public class SendAgent implements Agent {
    private final ConnectionHandler connectionHandler;
    private final long id;
    private final TaskBuilder taskBuilder;
    private final QueueHandler queueHandler;

    public SendAgent(ConnectionHandler connectionHandler, long id, TaskBuilder taskBuilder, QueueHandler queueHandler) {
        this.connectionHandler = connectionHandler;
        this.id = id;
        this.taskBuilder = taskBuilder;
        this.queueHandler = queueHandler;
    }

    @Override
    public void handle(PipelineContext context, Object data) {
        try {
            connectionHandler.writeToChannel((ByteBuffer) data, id);
        } catch (IOException e) {
            queueHandler.addTask(taskBuilder.getPeerDeletionRequest(id, "Exception while sending data: " + e.getMessage()));
        }
    }
}
