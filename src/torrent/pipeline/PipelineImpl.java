package torrent.pipeline;

import torrent.pipeline.agents.Agent;

import java.util.*;

public class PipelineImpl implements Pipeline {
    private final List<AgentContext> contexts = new ArrayList<>();

    private boolean idAssigned = false;
    private long id;

    @Override
    public Pipeline addAgent(Agent agent) {
        contexts.add(new AgentContext(this, contexts.size(), agent));
        return this;
    }

    @Override
    public long getId() {
        if(!idAssigned) {
            throw new UnassignedPipelineIdException();
        }
        return id;
    }


    @Override
    public void setId(long id) {
        if(idAssigned) {
            throw new IdReassignmentException();
        }
        this.id = id;
    }

    @Override
    public void sendToIndex(Object data, int index) {
        if(index >= 0 && index < contexts.size()) {
            AgentContext agentContext = contexts.get(index);
            agentContext.getAgent().handle(agentContext, data);
        }
    }
}
