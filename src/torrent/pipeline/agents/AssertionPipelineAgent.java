package torrent.pipeline.agents;

import org.junit.Assert;
import torrent.pipeline.AgentContext;

import java.util.function.Predicate;

public class AssertionPipelineAgent implements AgentInterface {
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
