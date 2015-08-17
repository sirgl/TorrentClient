package torrent.pipeline;

import java.io.IOException;

public interface PipelineControllerInterface {
    void send(Object data, long pipelineId) throws IOException;
    void addPipeline(PipelineInterface pipeline, long pipelineId) throws PipelineCreationException;
    void removePipeline(long pipelineId);
    void replacePipeline(PipelineInterface pipeline, long pipelineId);
}
