package torrent.pipeline.agents.general;

import torrent.pipeline.PipelineContext;
import torrent.pipeline.agents.Agent;

public class OutputAgent implements Agent {
    private Object result;

    public Object getResult() {
        return result;
    }

    @Override
    public void handle(PipelineContext context, Object data) {
        result = data;
        context.sendNext(data);
    }
}
