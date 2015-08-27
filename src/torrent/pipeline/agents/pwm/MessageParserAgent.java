package torrent.pipeline.agents.pwm;

import torrent.pipeline.PipelineContext;
import torrent.pipeline.agents.Agent;

import java.nio.ByteBuffer;

public class MessageParserAgent implements Agent {


    private final int bitfieldSize;

    public MessageParserAgent(int bitfieldSize) {
        this.bitfieldSize = bitfieldSize;
    }

    @Override
    public void handle(PipelineContext context, Object data) {
        ByteBuffer buffer = (ByteBuffer) data;
        byte messageId = buffer.get();
        switch (messageId) {
            case 4:
                System.out.println("Have: " + buffer.getInt() + " piece");
                break;
            case 5:
                byte[] bitfield = new byte[bitfieldSize];
                if(buffer.limit() - buffer.position() < bitfieldSize) {
                    System.out.println("Bad bitfield");
                    break;
                }
                buffer.get(bitfield);
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
}
