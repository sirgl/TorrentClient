package torrent.pipeline;

public interface PipelineController {
    void send(Object data, long pipelineId);

    void addPipeline(Pipeline pipeline, long pipelineId) throws PipelineCreationException;

    void removePipeline(long pipelineId);

    void replacePipeline(Pipeline pipeline, long pipelineId);
}
