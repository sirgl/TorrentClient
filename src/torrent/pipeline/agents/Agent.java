package torrent.pipeline.agents;

import torrent.pipeline.PipelineContext;

public interface Agent {
    void handle(PipelineContext context, Object data);
}
