package torrent.pipeline.agents.general;

import torrent.pipeline.PipelineContext;
import torrent.pipeline.Pipeline;
import torrent.pipeline.agents.Agent;

import java.nio.ByteBuffer;


/**
 * Agent for logging raw binary data in form of ByteBuffer
 */
public class ByteBufferLoggerAgent implements Agent {
    private final Pipeline pipeline;

    public ByteBufferLoggerAgent(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    /**
     * @param data buffer that must be flipped(pos = 0, limit at the end of data)
     */
    @Override
    public void handle(PipelineContext context, Object data) {
        ByteBuffer buffer = (ByteBuffer) data;
        System.out.println("Buffer " + buffer.limit() + "/" + buffer.capacity());
        System.out.print("|");
        while (buffer.hasRemaining()) {
            System.out.print(String.format("0x%02X", buffer.get()));
            System.out.print("|");
        }
        System.out.print("\n");
        buffer.flip();
        context.sendNext(data);
    }
}
