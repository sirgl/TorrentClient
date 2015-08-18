package torrent.pipeline.agents.general;

import org.junit.Assert;
import torrent.pipeline.AgentContext;
import torrent.pipeline.agents.Agent;

import java.util.function.Predicate;

public class AssertionPipelineAgent implements Agent {
    private final Predicate predicate;

    public AssertionPipelineAgent(Predicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public void handle(AgentContext context, Object data) {
        Assert.assertTrue(predicate.test(data));
        context.sendNext(data);
    }
}
