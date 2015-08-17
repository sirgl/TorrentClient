package torrent.pipeline;

import torrent.pipeline.agents.AgentInterface;

import java.util.Iterator;

public class AgentContext {
    private final PipelineInterface pipeline;
    private final int index;
    private final AgentInterface agentInterface;

    public AgentContext(PipelineInterface pipeline, int index, AgentInterface agentInterface) {
        this.pipeline = pipeline;
        this.index = index;
        this.agentInterface = agentInterface;
    }

    public void sendNext(Object data) {
        pipeline.sendToIndex(data, index + 1);
    }

    AgentInterface getAgent(){
        return agentInterface;
    }
}
