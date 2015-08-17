package torrent.pipeline;

import torrent.pipeline.agents.AgentInterface;

public interface PipelineInterface {
    PipelineInterface addAgent(AgentInterface agent);

    void sendNext(AgentInterface currentAgent, Object data);
}
