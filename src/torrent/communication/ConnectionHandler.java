package torrent.communication;

import torrent.pipeline.Pipeline;
import torrent.pipeline.PipelineController;
import torrent.pipeline.PipelineControllerInterface;
import torrent.pipeline.PipelineCreationException;
import torrent.pipeline.agents.ByteBufferLoggerAgent;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class ConnectionHandler implements Runnable {
    /**
     * @implNote Must be greater, than 64
     */
    private static final int BUFFER_CAPACITY = 1024;
    private static long idCounter = 0;
    private final Selector selector;
    private final ServerSocketChannel serverSocketChannel;
    private final ByteBuffer buffer;
    private final InetSocketAddress serverPort;
    private final PeerManagerInterface peerManager;
    private final CommunicationService communicationService;

    private final PipelineControllerInterface incomingPipelineController = new PipelineController();
    private final PipelineControllerInterface outgoingPipelineController = new PipelineController();


    public ConnectionHandler(InetSocketAddress serverPort, PeerManagerInterface manager, CommunicationService communicationService) throws IOException {
        this.serverPort = serverPort;
        this.peerManager = manager;
        this.communicationService = communicationService;
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        buffer = ByteBuffer.allocateDirect(BUFFER_CAPACITY);
        //TODO run HeartbeatService
    }

    @Override
    public void run() {
        try {
            serverSocketChannel.bind(serverPort);
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (!Thread.interrupted()) {
                int selectedCount = selector.select();
                if (selectedCount == 0) {
                    continue;
                }
                for (Iterator<SelectionKey> iterator = selector.selectedKeys().iterator(); iterator.hasNext(); ) {
                    SelectionKey selectionKey = iterator.next();
                    try {
                        if (selectionKey.isAcceptable()) {
                            accept(((ServerSocketChannel) selectionKey.channel()).accept());
                        }
                        if (selectionKey.isReadable()) {
                            read((SocketChannel) selectionKey.channel(), (Long)selectionKey.attachment());
                        }

                    }
                    catch (IOException e) {
                        long id = (Long) selectionKey.attachment();
                        peerManager.removePeer(id);
                        removePipelines(id);
                    }
                    finally {
                        iterator.remove();
                    }
                }
            }
        } catch (IOException e) {
            //Strange exception that can't let app be running
            //TODO
        }
    }

    private void removePipelines(long id) {
        incomingPipelineController.removePipeline(id);
        outgoingPipelineController.removePipeline(id);
    }

    private void read(SocketChannel socketChannel, long id) throws IOException {
        buffer.clear();
        int read = socketChannel.read(buffer);
        buffer.flip();
        incomingPipelineController.send(buffer, id);
    }

    private void accept(SocketChannel socketChannel) throws IOException {
        long id = addPeerToPipeline();
        peerManager.addPeer(id);
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ, id);
    }

    private long getNextId() {
        while (peerManager.hasPeer(idCounter)) {
            idCounter++;
        }
        return idCounter;
    }


    /**
     * @return peer id
     */
    public long addPeerToPipeline() throws IOException {
        try {
            long id = getNextId();
            Pipeline pipeline = new Pipeline();
            pipeline.addAgent(new ByteBufferLoggerAgent(pipeline));
            incomingPipelineController.addPipeline(pipeline, id);
            outgoingPipelineController.addPipeline(new Pipeline(), id);
            return id;
        } catch (PipelineCreationException e) {
            throw new IOException(e);
        }
    }

    public void connectToPeer(SocketAddress socketAddress) throws IOException {
        SocketChannel socketChannel = SocketChannel.open(socketAddress);
    }
}
