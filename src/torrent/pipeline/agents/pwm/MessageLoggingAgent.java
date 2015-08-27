package torrent.pipeline.agents.pwm;

import torrent.Tools;
import torrent.pipeline.PipelineContext;
import torrent.pipeline.agents.Agent;

import java.nio.ByteBuffer;

public class MessageLoggingAgent implements Agent {


    private int bitfieldSize;

    public MessageLoggingAgent(int pieceCount) {
        this.bitfieldSize = (pieceCount % 8 == 0) ? (pieceCount / 8) : (pieceCount / 8 + 1);
    }


    @Override
    public void handle(PipelineContext context, Object data) {
        ByteBuffer buffer = (ByteBuffer) data;
        byte messageId = buffer.get();

        System.out.println("Message(id = " + context.getId() + ", size = " + (buffer.limit() - buffer.position()) + ")");
        switch (messageId) {
            case 0:
                System.out.println("Choke");
                break;
            case 1:
                System.out.println("Unchoke");
                break;
            case 2:
                System.out.println("Interested");
                break;
            case 3:
                System.out.println("Uninterested");
                break;
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
                System.out.println("Bitfield: " + Tools.toBinary(bitfield));
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
            case 8:
                System.out.println("Cancel");
                System.out.print("piece index = " + buffer.getInt());
                System.out.print("block offset = " + buffer.getInt());
                System.out.print("block length = " + buffer.getInt());
                break;
            default:
                System.out.println("Unhandled message: id = " + messageId);
                System.out.println("Size = " + buffer.capacity());
        }
        buffer.flip();
        context.sendNext(data);
    }
}
