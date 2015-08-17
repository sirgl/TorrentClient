package torrent.pipeline.agents;

import torrent.pipeline.PipelineInterface;

import java.nio.ByteBuffer;

/**
 * Agent for assembling peer wire protocol(PWP) messages.
 * It compiles solid message from many buffers.
 */
public class MessageAssemblerAgent implements AgentInterface {
    private static final int SIZE_LENGTH = 4;
    private static final int INTERNAL_BUFFER_CAPACITY = 256;

    private final ByteBuffer sizeBuffer = ByteBuffer.allocateDirect(SIZE_LENGTH);
    private final PipelineInterface pipeline;
    private final byte[] internalBuffer = new byte[INTERNAL_BUFFER_CAPACITY];
    private ByteBuffer dataBuffer;

    public MessageAssemblerAgent(PipelineInterface pipeline) {
        this.pipeline = pipeline;
    }

    /**
     * @param data - must be flipped buffer that contains part of PWP message
     */
    @Override
    public void handle(Object data) {
        ByteBuffer buffer = (ByteBuffer) data;
        if (sizeBuffer.hasRemaining()) {
            int neededCount = sizeBuffer.limit() - sizeBuffer.position();
            int availableCount = buffer.limit() - buffer.position();
            int min = Math.min(Math.min(neededCount, availableCount), INTERNAL_BUFFER_CAPACITY);
            buffer.get(internalBuffer, 0, min);
            sizeBuffer.put(internalBuffer, 0, min);
            if (!sizeBuffer.hasRemaining()) {
                sizeBuffer.flip();
                dataBuffer = ByteBuffer.allocate(sizeBuffer.getInt());
            } else {
                return;
            }
        }
        if (dataBuffer.hasRemaining()) {
            int neededCount = dataBuffer.limit() - dataBuffer.position();
            int availableCount = buffer.limit() - buffer.position();
            int min = Math.min(Math.min(neededCount, availableCount), INTERNAL_BUFFER_CAPACITY);
            buffer.get(internalBuffer, 0, min);
            dataBuffer.put(internalBuffer, 0, min);
            if (!dataBuffer.hasRemaining()) {
                dataBuffer.flip();
                pipeline.sendNext(this, dataBuffer);
            }
        }
    }
}