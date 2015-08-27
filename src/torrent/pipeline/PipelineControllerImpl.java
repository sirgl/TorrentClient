package torrent.pipeline;

import java.util.HashMap;
import java.util.Map;

public class PipelineControllerImpl implements PipelineController {
    private final Map<Long, Pipeline> pipelineMap = new HashMap<>();

    @Override
    public void send(Object data, long pipelineId) {
        Pipeline pipeline = pipelineMap.get(pipelineId);
        if (pipeline == null) {
            throw new WrongPipelineIdException(pipelineId);
        }

        pipeline.sendToIndex(data, 0);
    }

    @Override
    public void addPipeline(Pipeline pipeline, long pipelineId) throws PipelineCreationException {
        Pipeline mapInstance = pipelineMap.get(pipelineId);
        if (mapInstance != null) {
            throw new PipelineCreationException("Pipeline with id '" + pipelineId + "' already exists");
        }
        pipeline.setId(pipelineId);
        pipelineMap.put(pipelineId, pipeline);
    }

    @Override
    public void removePipeline(long pipelineId) {
        pipelineMap.remove(pipelineId);
    }

    @Override
    public void replacePipeline(Pipeline pipeline, long pipelineId) {
        pipeline.setId(pipelineId);
        pipelineMap.put(pipelineId, pipeline);
    }
}
