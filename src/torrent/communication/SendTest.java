package torrent.communication;

import torrent.queue.QueueHandler;
import torrent.queue.QueueHandlerImpl;
import torrent.queue.TaskBuilderImpl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SendTest {
    public static void main(String[] args) throws IOException {
        PeerManagerImpl peerManager = new PeerManagerImpl();
        TaskBuilderImpl taskBuilder = new TaskBuilderImpl(peerManager);
        QueueHandler queueHandler = new QueueHandlerImpl();
        ConnectionHandler connectionHandler = new ConnectionHandler(new InetSocketAddress(4444),
                taskBuilder, queueHandler, new byte[20]);
        CommunicationService communicationService = new CommunicationService(connectionHandler);
        taskBuilder.setSendService(connectionHandler);
        taskBuilder.setCommunicationController(communicationService);
        new Thread(connectionHandler).start();
        new Thread((Runnable) queueHandler).start();

        new Thread(() -> {
            try {
                SocketChannel channel = SocketChannel.open();
                channel.connect(new InetSocketAddress("localhost", 4444));
                ByteBuffer buffer = ByteBuffer.allocateDirect(500);
                buffer.put((byte) 19);
                buffer.put("BitTorrent protocol".getBytes());
                buffer.put(new byte[8]);
                buffer.put(new byte[20]);
                buffer.put(new byte[20]);
                buffer.flip();
                channel.write(buffer);
                buffer.clear();
                buffer.put((byte) 5);
                buffer.putInt(5);
                buffer.flip();
                channel.write(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }
}
