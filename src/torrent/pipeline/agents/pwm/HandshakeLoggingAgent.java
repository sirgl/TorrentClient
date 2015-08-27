package torrent.pipeline.agents.pwm;

import torrent.pipeline.PipelineContext;
import torrent.pipeline.agents.Agent;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class HandshakeLoggingAgent implements Agent {
    public static final int EXTENSIONS_LENGTH = 8;
    public static final int INFO_HASH_LENGTH = 20;
    public static final int PEER_NAME_LENGTH = 20;
    private static final byte PROTOCOL_NAME_LENGTH = 19;

    @Override
    public void handle(PipelineContext context, Object data) {
        ByteBuffer incomeBuffer = (ByteBuffer) data;
        byte protocolNameSize = incomeBuffer.get();
        byte[] protocolName = new byte[PROTOCOL_NAME_LENGTH];
        incomeBuffer.get(protocolName);
        byte[] extensions = new byte[EXTENSIONS_LENGTH];
        incomeBuffer.get(extensions);
        byte[] infoHash = new byte[INFO_HASH_LENGTH];
        incomeBuffer.get(infoHash);
        byte[] peerName = new byte[PEER_NAME_LENGTH];
        incomeBuffer.get(peerName);
        incomeBuffer.flip();
        System.out.println("Protocol name size: " + protocolNameSize);
        System.out.println("Protocol: " + new String(protocolName));
        System.out.println("InfoHash: " + Arrays.toString(infoHash));
        System.out.println("Peer name: " + new String(peerName));
        context.sendNext(data);
    }
}
