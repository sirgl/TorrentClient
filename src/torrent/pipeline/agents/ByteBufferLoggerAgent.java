package torrent.pipeline.agents;

import torrent.pipeline.PipelineInterface;

import java.nio.ByteBuffer;


/**
 * Agent for logging raw binary data in form of ByteBuffer
 */
public class ByteBufferLoggerAgent implements AgentInterface {
    private final PipelineInterface pipeline;

    public ByteBufferLoggerAgent(PipelineInterface pipeline) {
        this.pipeline = pipeline;
    }

    /**
     * @param data buffer that must be flipped(pos = 0, limit at the end of data)
     */
    @Override
    public void handle(Object data) {
        ByteBuffer buffer = (ByteBuffer) data;
        System.out.println("Buffer " + buffer.limit() + "/" + buffer.capacity());
        System.out.print("|");
        while (buffer.hasRemaining()) {
            System.out.print(String.format("0x%02X", buffer.get()));
            System.out.print("|");
        }
        System.out.print("\n");
        buffer.flip();
        pipeline.sendNext(this, data);
    }
}
