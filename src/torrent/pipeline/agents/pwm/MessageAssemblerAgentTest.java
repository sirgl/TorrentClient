package torrent.pipeline.agents.pwm;

import junit.framework.TestCase;
import org.junit.Assert;
import torrent.pipeline.PipelineImpl;
import torrent.pipeline.PipelineControllerImpl;
import torrent.pipeline.PipelineController;
import torrent.pipeline.Pipeline;
import torrent.pipeline.agents.general.OutputAgent;

import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;

public class MessageAssemblerAgentTest extends TestCase {
    private Pipeline pipeline;
    private final PipelineController pipelineController = new PipelineControllerImpl();
    private final OutputAgent exitAgent = new OutputAgent();


    @Override
    public void setUp() throws Exception {
        pipeline = new PipelineImpl();
        pipeline.addAgent(new MessageAssemblerAgent(pipeline))
                .addAgent(exitAgent);
        pipelineController.replacePipeline(pipeline, 0);
    }

    public void testSizeCorrect() throws IOException {
        pipelineController.send(ByteBuffer.allocate(4).putInt(10).flip(), 0);
        Buffer data = ByteBuffer.allocate(10).put("helloworld".getBytes()).flip();
        pipelineController.send(data, 0);
        ByteBuffer result = (ByteBuffer) exitAgent.getResult();
        Buffer mustBe = ByteBuffer.allocate(10).put("helloworld".getBytes()).flip();
        Assert.assertEquals(mustBe, result);
    }

    public void testSeparatedDataWithSinglePartedSize() throws IOException {
        pipelineController.send(ByteBuffer.allocate(4).putInt(10).flip(), 0);
        Buffer data1 = ByteBuffer.allocate(10).put("hello".getBytes()).flip();
        Buffer data2 = ByteBuffer.allocate(10).put("world".getBytes()).flip();
        pipelineController.send(data1, 0);
        pipelineController.send(data2, 0);
        ByteBuffer result = (ByteBuffer) exitAgent.getResult();
        Buffer mustBe = ByteBuffer.allocate(10).put("helloworld".getBytes()).flip();
        Assert.assertEquals(mustBe, result);
    }

    public void testSeparatedSize() throws IOException {
        ByteBuffer size1 = ByteBuffer.allocate(2).put((byte) 0).put((byte) 0);
        ByteBuffer size2 = ByteBuffer.allocate(2).put((byte) 0).put((byte) 10);
        size1.flip();
        size2.flip();
        pipelineController.send(size1, 0);
        pipelineController.send(size2, 0);
        Buffer data = ByteBuffer.allocate(10).put("helloworld".getBytes()).flip();
        pipelineController.send(data, 0);
        Buffer mustBe = ByteBuffer.allocate(10).put("helloworld".getBytes()).flip();
        ByteBuffer result = (ByteBuffer) exitAgent.getResult();
        Assert.assertEquals(mustBe, result);
    }

}