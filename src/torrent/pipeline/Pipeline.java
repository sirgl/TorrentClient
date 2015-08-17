package torrent.pipeline;

import torrent.pipeline.agents.AgentInterface;

import java.util.*;

public class Pipeline implements PipelineInterface {
    private final List<AgentContext> contexts = new ArrayList<>();

    @Override
    public PipelineInterface addAgent(AgentInterface agent) {
        contexts.add(new AgentContext(this, contexts.size(), agent));
        return this;
    }

    @Override
    public void sendToIndex(Object data, int index) {
        if(index >= 0 && index < contexts.size()) {
            AgentContext agentContext = contexts.get(index);
            agentContext.getAgent().handle(agentContext, data);
        }
    }
}
