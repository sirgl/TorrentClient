package torrent.pipeline;

import torrent.pipeline.agents.Agent;

public class PipelineContext {
    private final Pipeline pipeline;
    private final int index;
    private final Agent agent;

    public PipelineContext(Pipeline pipeline, int index, Agent agent) {
        this.pipeline = pipeline;
        this.index = index;
        this.agent = agent;
    }

    public void sendNext(Object data) {
        pipeline.sendToIndex(data, index + 1);
    }

    Agent getAgent(){
        return agent;
    }

    public long getId() {
        return pipeline.getId();
    }
}
