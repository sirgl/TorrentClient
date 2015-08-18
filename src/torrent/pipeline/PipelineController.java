package torrent.pipeline;

import java.io.IOException;

public interface PipelineController {
    void send(Object data, long pipelineId) throws IOException;
    void addPipeline(Pipeline pipeline, long pipelineId) throws PipelineCreationException;
    void removePipeline(long pipelineId);
    void replacePipeline(Pipeline pipeline, long pipelineId);
}
