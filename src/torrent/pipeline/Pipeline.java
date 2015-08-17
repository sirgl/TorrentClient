package torrent.pipeline;

import torrent.pipeline.agents.AgentInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pipeline implements PipelineInterface {
    private final List<AgentInterface> agentList = new ArrayList<>();
    private final Map<AgentInterface, AgentInterface> nextAgentMap = new HashMap<>();


    @Override
    public PipelineInterface addAgent(AgentInterface agent) {
        if(agentList.size() > 0) {
            AgentInterface oldLastAgent = agentList.get(agentList.size() - 1);
            nextAgentMap.put(oldLastAgent, agent);
        }
        nextAgentMap.put(agent, null);
        agentList.add(agent);
        return this;
    }

    @Override
    public void sendNext(AgentInterface currentAgent, Object data) {
        if(currentAgent == null) {
            agentList.get(0).handle(data);
        }
        AgentInterface nextAgent = nextAgentMap.get(currentAgent);
        if(nextAgent == null) {
            return;
        }
        nextAgent.handle(data);
    }
}
