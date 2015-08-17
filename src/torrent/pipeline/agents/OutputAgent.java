package torrent.pipeline.agents;

import torrent.pipeline.AgentContext;

public class OutputAgent implements AgentInterface {
    private Object result;

    public Object getResult() {
        return result;
    }

    @Override
    public void handle(AgentContext context, Object data) {
        result = data;
        context.sendNext(data);
    }
}
