package torrent.pipeline;

import torrent.pipeline.agents.Agent;

import java.util.*;

public class PipelineImpl implements Pipeline {
    private final List<PipelineContext> contexts = new ArrayList<>();

    private boolean idAssigned = false;
    private long id;

    @Override
    public Pipeline addAgent(Agent agent) {
        contexts.add(new PipelineContext(this, contexts.size(), agent));
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
        idAssigned = true;
    }

    @Override
    public void sendToIndex(Object data, int index) {
        if(index >= 0 && index < contexts.size()) {
            PipelineContext pipelineContext = contexts.get(index);
            pipelineContext.getAgent().handle(pipelineContext, data);
        }
    }
}
