package torrent.communication;

import torrent.queue.QueueHandler;
import torrent.queue.QueueHandlerImpl;
import torrent.queue.TaskBuilder;
import torrent.queue.TaskBuilderImpl;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SendTest {
    public static void main(String[] args) throws IOException {
        CommunicationService communicationService = new CommunicationService();
        PeerManagerImpl peerManager = new PeerManagerImpl();
        TaskBuilder taskBuilder = new TaskBuilderImpl(communicationService, communicationService, peerManager);
        QueueHandler queueHandler = new QueueHandlerImpl();
        ConnectionHandler connectionHandler = new ConnectionHandler(new InetSocketAddress(4444), peerManager, communicationService, taskBuilder, queueHandler, new byte[20]);
        new Thread(connectionHandler).start();

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
                buffer.putInt(1);
                buffer.put((byte) 5);
                buffer.flip();
                channel.write(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }
}
