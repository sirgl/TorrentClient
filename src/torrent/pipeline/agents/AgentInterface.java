package torrent.pipeline.agents;

import torrent.pipeline.AgentContext;

public interface AgentInterface {
    void handle(AgentContext context, Object data);
}
