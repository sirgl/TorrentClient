package torrent.pipeline.agents;

import torrent.pipeline.AgentContext;

public interface Agent {
    void handle(AgentContext context, Object data);
}
