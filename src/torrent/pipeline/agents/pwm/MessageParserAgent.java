package torrent.pipeline.agents.pwm;

import torrent.pipeline.PipelineContext;
import torrent.pipeline.agents.Agent;
import torrent.queue.QueueHandler;
import torrent.queue.TaskBuilder;

import java.nio.ByteBuffer;

public class MessageParserAgent implements Agent {


    private final int bitfieldSize;
    private final QueueHandler queueHandler;
    private final TaskBuilder taskBuilder;


    public MessageParserAgent(int bitfieldSize, QueueHandler queueHandler, TaskBuilder taskBuilder) {
        this.bitfieldSize = bitfieldSize;
        this.queueHandler = queueHandler;
        this.taskBuilder = taskBuilder;
    }

    @Override
    public void handle(PipelineContext context, Object data) {
        ByteBuffer buffer = (ByteBuffer) data;
        byte messageId = buffer.get();
        switch (messageId) {
            case 4:
                handleHaveMessage(context, buffer);
                break;
            case 5:
                handleBitfieldMessage(buffer, context);
                break;
            case 6:
                System.out.println("Request: ");
                System.out.print("piece index = " + buffer.getInt());
                System.out.print("block offset = " + buffer.getInt());
                System.out.print("block length = " + buffer.getInt());
                break;
            case 7:
                System.out.println("Piece");
                int pieceIndex = buffer.getInt();
                int blockOffset = buffer.getInt();
                ByteBuffer piece = buffer.slice();
                System.out.println("piece index = " + pieceIndex);
                System.out.println("block offset = " + blockOffset);
                System.out.println("piece size = " + piece.capacity());
                break;
        }

    }

    private void handleBitfieldMessage(ByteBuffer buffer, PipelineContext context) {
        byte[] bitfield = new byte[bitfieldSize];
        if(buffer.limit() - buffer.position() < bitfieldSize) {
            //TODO handle bad bitfield size
            return;
        }
        buffer.get(bitfield);
        queueHandler.addTask(taskBuilder.getBitfieldMessageHandleRequest(context.getId(), bitfield));
    }

    private void handleHaveMessage(PipelineContext context, ByteBuffer buffer) {
        int pieceNumber = buffer.getInt();
        queueHandler.addTask(taskBuilder.getHaveMessageHandleRequest(context.getId(), pieceNumber));
    }
}
