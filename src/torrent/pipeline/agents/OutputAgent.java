package torrent.pipeline.agents;

public class OutputAgent implements AgentInterface {
    private Object result;

    public Object getResult() {
        return result;
    }

    @Override
    public void handle(Object data) {
        result = data;
    }
}
