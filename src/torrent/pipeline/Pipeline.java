package torrent.pipeline;

import torrent.pipeline.agents.Agent;

public interface Pipeline {
    Pipeline addAgent(Agent agent);
    long getId();
    void sendToIndex(Object data, int index);

    /**
     *  uses by PipelineController to assign ids, don't use it together
     */
    void setId(long id);
}
