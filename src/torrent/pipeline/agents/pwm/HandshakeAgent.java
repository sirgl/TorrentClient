package torrent.pipeline.agents.pwm;

import torrent.communication.PeerManager;
import torrent.pipeline.AgentContext;
import torrent.pipeline.agents.Agent;
import torrent.queue.QueueHandler;
import torrent.queue.TaskBuilder;

import java.nio.ByteBuffer;
import java.util.Arrays;


public class HandshakeAgent implements Agent {
    public static final int EXTENSIONS_LENGTH = 8;
    public static final int INFO_HASH_LENGTH = 20;
    public static final int PEER_NAME_LENGTH = 20;
    private static final byte PROTOCOL_NAME_LENGTH = 19;
    private static final byte[] PROTOCOL_NAME = "BitTorrent protocol".getBytes();


    private final QueueHandler queueHandler;
    private final PeerManager peerManager;
    private final byte[] ourInfoHash;
    private final TaskBuilder taskBuilder;

    public HandshakeAgent(QueueHandler queueHandler, PeerManager peerManager,
                          TaskBuilder taskBuilder, byte[] ourInfoHash) {
        this.queueHandler = queueHandler;
        this.peerManager = peerManager;
        this.ourInfoHash = ourInfoHash;
        this.taskBuilder = taskBuilder;
    }

    /**
     * @param data buffer exactly of handshake message size
     */
    @Override
    public void handle(AgentContext context, Object data) {
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
        peerManager.registerPeer(id, ByteBuffer.wrap(peerName));
    }
}
