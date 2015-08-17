package torrent.pipeline;

import torrent.pipeline.agents.AgentInterface;

public interface PipelineInterface {
    PipelineInterface addAgent(AgentInterface agent);

    void sendToIndex(Object data, int index);
}
