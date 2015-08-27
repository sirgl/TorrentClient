package torrent.pipeline.agents.pwm;

import torrent.pipeline.PipelineContext;
import torrent.pipeline.Pipeline;
import torrent.pipeline.PipelineController;
import torrent.pipeline.PipelineImpl;
import torrent.pipeline.agents.Agent;
import torrent.queue.QueueHandler;
import torrent.queue.TaskBuilder;

import java.nio.ByteBuffer;
import java.util.Arrays;


/**
 * Ending
 */
public class HandshakeAgent implements Agent {
    public static final int EXTENSIONS_LENGTH = 8;
    public static final int INFO_HASH_LENGTH = 20;
    public static final int PEER_NAME_LENGTH = 20;
    private static final byte PROTOCOL_NAME_LENGTH = 19;
    private static final byte[] PROTOCOL_NAME = "BitTorrent protocol".getBytes();


    private final QueueHandler queueHandler;
    private final byte[] ourInfoHash;
    private final TaskBuilder taskBuilder;
    private final PipelineController pipelineController;
    private final MessageAssemblerAgent previousAgent;

    /**
     * @param bufferingAgent previous agent that has a buffer(only previous buffer will be transmitted to new pipeline)
     */
    public HandshakeAgent(QueueHandler queueHandler,
                          TaskBuilder taskBuilder,
                          byte[] ourInfoHash,
                          PipelineController pipelineController,
                          MessageAssemblerAgent bufferingAgent) {
        this.queueHandler = queueHandler;
        this.ourInfoHash = ourInfoHash;
        this.taskBuilder = taskBuilder;
        this.pipelineController = pipelineController;
        this.previousAgent = bufferingAgent;
    }

    /**
     * @param data buffer exactly of handshake message size
     */
    @Override
    public void handle(PipelineContext context, Object data) {
        long id = context.getId();
        ByteBuffer incomeBuffer = (ByteBuffer) data;
        byte protocolNameSize = incomeBuffer.get();
        if (PROTOCOL_NAME_LENGTH != protocolNameSize) {
            queueHandler.addTask(taskBuilder.getPeerDeletionRequest(id, "Wrong size of version of protocol"));
            return;
        }
        byte[] protocolName = new byte[PROTOCOL_NAME_LENGTH];
        incomeBuffer.get(protocolName);
        if (!Arrays.equals(protocolName, PROTOCOL_NAME)) {
            queueHandler.addTask(taskBuilder.getPeerDeletionRequest(id, "Wrong protocol name"));
            return;
        }
        byte[] extensions = new byte[EXTENSIONS_LENGTH];
        incomeBuffer.get(extensions);
        byte[] infoHash = new byte[INFO_HASH_LENGTH];
        incomeBuffer.get(infoHash);
        if (!Arrays.equals(ourInfoHash, infoHash)) {
            queueHandler.addTask(taskBuilder.getPeerDeletionRequest(id, "Hash code incorrect"));
            return;
        }
        byte[] peerName = new byte[PEER_NAME_LENGTH];
        incomeBuffer.get(peerName);


        queueHandler.addTask(taskBuilder.getPeerRegistrationRequest(id, peerName));
        replacePipeline(id);
    }

    private void replacePipeline(long id) {
        Pipeline pipeline = new PipelineImpl();
        pipeline.addAgent(new MessageLoggingAgent(32))
                .addAgent(new MessageAssemblerAgent(pipeline));
        //TODO add message parser agent
        pipelineController.replacePipeline(pipeline, id);
        pipelineController.send(previousAgent.getInternalBuffer(), id);
    }

}
