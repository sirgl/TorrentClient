package torrent.pipeline;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PipelineController implements PipelineControllerInterface {
    private final Map<Long, PipelineInterface> pipelineMap = new HashMap<>();

    @Override
    public void send(Object data, long pipelineId) throws IOException {
        PipelineInterface pipeline = pipelineMap.get(pipelineId);
        if(pipeline == null) {
            throw new IOException("No pipeline with id'" + pipelineId + "'");
        }
        pipeline.sendNext(null, data);
    }

    @Override
    public void addPipeline(PipelineInterface pipeline, long pipelineId) throws PipelineCreationException{
        PipelineInterface mapInstance = pipelineMap.get(pipelineId);
        if (mapInstance != null) {
            throw new PipelineCreationException("Pipeline with id '" + pipelineId + "' already exists");
        }
        pipelineMap.put(pipelineId, pipeline);
    }

    @Override
    public void removePipeline(long pipelineId) {
        pipelineMap.remove(pipelineId);
    }

    @Override
    public void replacePipeline(PipelineInterface pipeline, long pipelineId) {
        pipelineMap.put(pipelineId, pipeline);
    }
}
