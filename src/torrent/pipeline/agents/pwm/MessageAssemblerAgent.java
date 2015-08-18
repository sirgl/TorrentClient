package torrent.pipeline.agents.pwm;

import torrent.pipeline.AgentContext;
import torrent.pipeline.Pipeline;
import torrent.pipeline.agents.Agent;

import java.nio.ByteBuffer;

import static torrent.tools.transferAsMuchAsPossible;

/**
 * Agent for assembling peer wire protocol(PWP) messages.
 * It compiles solid message from many buffers.
 */
public class MessageAssemblerAgent implements Agent {
    private static final int SIZE_LENGTH = 4;
    private static final int INTERNAL_BUFFER_CAPACITY = 256;

    private final ByteBuffer sizeBuffer = ByteBuffer.allocateDirect(SIZE_LENGTH);
    private final Pipeline pipeline;
    private final int size;
    private final byte[] internalBuffer = new byte[INTERNAL_BUFFER_CAPACITY];
    private ByteBuffer dataBuffer;
    private ByteBuffer buffer;

    private boolean stopped = false;

    /**
     * Creates agent, that will assemble message of size of first int in buffer
     * (expected data: |size|message|)
     */
    public MessageAssemblerAgent(Pipeline pipeline) {
        this(pipeline, 0);
    }

    /**
     * No size at the beginning expected
     * @param size size of the message
     */
    public MessageAssemblerAgent(Pipeline pipeline, int size) {
        this.pipeline = pipeline;
        this.size = size;
    }

    public ByteBuffer getInternalBuffer() {
        return buffer;
    }

    public void stop() {
        stopped = true;
    }

    /**
     * @param data must be flipped buffer that contains part of PWP message
     */
    @Override
    public void handle(AgentContext context, Object data) {
        buffer = (ByteBuffer) data;
        while(buffer.hasRemaining() && !stopped) {
            if (size == 0) {
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
            } else {
                if (dataBuffer == null) {
                    dataBuffer = ByteBuffer.allocate(size);
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
                    context.sendNext(dataBuffer);
                }
            }
        }
    }
}