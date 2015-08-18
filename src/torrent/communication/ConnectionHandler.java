package torrent.communication;

import torrent.pipeline.PipelineImpl;
import torrent.pipeline.PipelineControllerImpl;
import torrent.pipeline.PipelineController;
import torrent.pipeline.PipelineCreationException;
import torrent.pipeline.agents.pwm.HandshakeAgent;
import torrent.pipeline.agents.pwm.HandshakeLoggingAgent;
import torrent.pipeline.agents.pwm.MessageAssemblerAgent;
import torrent.queue.QueueHandler;
import torrent.queue.TaskBuilder;

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
     * @implNote Must be >69 (length of handshake message)
     */
    private static final int BUFFER_CAPACITY = 1024;
    public static final int HANDSHAKE_SIZE = 68;

    private static long idCounter = 0;
    private final Selector selector;
    private final ServerSocketChannel serverSocketChannel;
    private final ByteBuffer buffer;
    private final InetSocketAddress serverPort;
    private final PeerManager peerManager;
    private final CommunicationService communicationService;
    private final TaskBuilder taskBuilder;
    private final QueueHandler queueHandler;

    private final PipelineController incomingPipelineController = new PipelineControllerImpl();
    private final PipelineController outgoingPipelineController = new PipelineControllerImpl();
    private final byte[] infoHash;


    public ConnectionHandler(InetSocketAddress serverPort,
                             PeerManager manager,
                             CommunicationService communicationService,
                             TaskBuilder taskBuilder,
                             QueueHandler queueHandler, byte[] infoHash) throws IOException {
        this.serverPort = serverPort;
        this.peerManager = manager;
        this.communicationService = communicationService;
        this.taskBuilder = taskBuilder;
        this.queueHandler = queueHandler;
        this.infoHash = infoHash;
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
                        queueHandler.addTask(taskBuilder.getPeerDeletionRequest(id, e.getMessage()));
                    }
                    finally {
                        iterator.remove();
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e);
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
            PipelineImpl pipeline = new PipelineImpl();
            MessageAssemblerAgent agent = new MessageAssemblerAgent(pipeline, HANDSHAKE_SIZE);
            pipeline.addAgent(agent)
                    .addAgent(new HandshakeLoggingAgent())
                    .addAgent(new HandshakeAgent(queueHandler, taskBuilder, infoHash, incomingPipelineController, agent));
            incomingPipelineController.addPipeline(pipeline, id);
            outgoingPipelineController.addPipeline(new PipelineImpl(), id);
            return id;
        } catch (PipelineCreationException e) {
            throw new IOException(e);
        }
    }

    public void connectToPeer(SocketAddress socketAddress) throws IOException {
        SocketChannel socketChannel = SocketChannel.open(socketAddress);
    }
}
