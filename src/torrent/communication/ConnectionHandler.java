package torrent.communication;

import torrent.pipeline.*;
import torrent.pipeline.agents.general.SendAgent;
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
import java.util.*;

public class ConnectionHandler implements Runnable, SendService {
    public static final int HANDSHAKE_SIZE = 68;
    /**
     * @implNote Must be >69 (length of handshake message)
     */
    private static final int BUFFER_CAPACITY = 1024;
    private static long idCounter = 0;
    private final Selector selector;
    private final ServerSocketChannel serverSocketChannel;
    private final ByteBuffer buffer;
    private final InetSocketAddress serverPort;
    private final TaskBuilder taskBuilder;
    private final QueueHandler queueHandler;

    private final PipelineController incomingPipelineController = new PipelineControllerImpl();
    private final PipelineController outgoingPipelineController = new PipelineControllerImpl();
    private final byte[] infoHash;

    private final Queue<Runnable> taskQueue = new ArrayDeque<>();
    private final Map<Long, SocketChannel> idMap = new HashMap<>();


    public ConnectionHandler(InetSocketAddress serverPort,
                             TaskBuilder taskBuilder,
                             QueueHandler queueHandler, byte[] infoHash) throws IOException {
        this.serverPort = serverPort;
        this.taskBuilder = taskBuilder;
        this.queueHandler = queueHandler;
        this.infoHash = infoHash;
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        buffer = ByteBuffer.allocateDirect(BUFFER_CAPACITY);
        //TODO run HeartbeatService
    }

    public void addTask(Runnable task) {
        taskQueue.add(task);
        selector.wakeup();
    }

    @Override
    public void run() {
        try {
            serverSocketChannel.bind(serverPort);
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (!Thread.interrupted()) {
                handleQueue();
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
                            read((SocketChannel) selectionKey.channel(), (Long) selectionKey.attachment());
                        }
                    } catch (IOException e) {
                        long id = (Long) selectionKey.attachment();
                        selectionKey.cancel();
                        removePipelines(id);
                        queueHandler.addTask(taskBuilder.getPeerDeletionRequest(id, e.getMessage()));
                    } finally {
                        iterator.remove();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            //Strange exception that can't let app be running
            //TODO
        }
    }


    /**
     * writes data to channel avoiding pipeline
     */
    public void writeToChannel(ByteBuffer buffer, long id) throws IOException {
        idMap.get(id).write(buffer);
    }

    private void handleQueue() {
        while (!taskQueue.isEmpty()) {
            taskQueue.remove().run();
        }
    }

    public void removePeer(long id) {
        SocketChannel socketChannel = idMap.remove(id);
        socketChannel.keyFor(selector).cancel();
    }

    public void removePipelines(long id) {
        incomingPipelineController.removePipeline(id);
        outgoingPipelineController.removePipeline(id);
    }

    private void read(SocketChannel socketChannel, long id) throws IOException {
        buffer.clear();
        socketChannel.read(buffer);
        buffer.flip();
        incomingPipelineController.send(buffer, id);
    }

    private void accept(SocketChannel socketChannel) throws IOException {
        long id = addPeerToPipeline();
        queueHandler.addTask(taskBuilder.getPeerCreationRequest(id));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ, id);
        idMap.put(id, socketChannel);
    }

    private long getNextId() {
        while(idMap.get(idCounter) != null) {
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
            Pipeline pipeline = new PipelineImpl();
            MessageAssemblerAgent agent = new MessageAssemblerAgent(pipeline, HANDSHAKE_SIZE);
            pipeline.addAgent(agent)
                    .addAgent(new HandshakeLoggingAgent())
                    .addAgent(new HandshakeAgent(queueHandler, taskBuilder, infoHash, incomingPipelineController, agent));
            incomingPipelineController.addPipeline(pipeline, id);
            Pipeline outPipeline = new PipelineImpl();
            outPipeline.addAgent(new SendAgent(this, id, taskBuilder, queueHandler));
            outgoingPipelineController.addPipeline(outPipeline, id);
            return id;
        } catch (PipelineCreationException e) {
            throw new IOException(e);
        }
    }

    public void connectToPeer(SocketAddress socketAddress) throws IOException {
        SocketChannel socketChannel = SocketChannel.open(socketAddress);
        accept(socketChannel);
    }

    /**
     * Uses to send data through outgoing pipeline
     * Can be used only by queue handler to avoid deadlock
     */
    @Override
    public void sendMessage(byte[] message, long peerId) {
        ByteBuffer buffer = ByteBuffer.wrap(message);
        outgoingPipelineController.send(buffer, peerId);
    }
}
