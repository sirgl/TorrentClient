package torrent;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class testServer {

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(4444));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        Map<Integer, SocketChannel> channelMap = new HashMap<>();

        Thread heartbeatService = new Thread(() -> {
            while(true) {
                for (SocketChannel socketChannel : channelMap.values()) {
                    try {
                        socketChannel.write(ByteBuffer.wrap("Hello".getBytes()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        heartbeatService.start();

        int messageNum = 0;
        while (true) {
            int selectedCount = selector.select();
            if (selectedCount == 0) {
                continue;
            }
            int peerNum = 0;
            for (Iterator<SelectionKey> iterator = selector.selectedKeys().iterator(); iterator.hasNext(); ) {
                SelectionKey selectionKey = iterator.next();
                try {
                    iterator.remove();
                    if(selectionKey.isAcceptable()) {
                        SocketChannel clientChannel = serverSocketChannel.accept();
                        clientChannel.configureBlocking(false);
                        clientChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                        System.out.println("user accepted");
                        channelMap.put(peerNum++, clientChannel);
                    }
                    if (selectionKey.isReadable()) {
                        ByteBuffer buffer = ByteBuffer.allocate(2);
                        int byteRead = 0;
                        while((byteRead = ((ReadableByteChannel)selectionKey.channel()).read(buffer)) > 0) {
                            if (byteRead == -1) {
                                throw new IOException("Read on closed key");
                            }
                            buffer.flip();
                            int count = 0;
                            while (buffer.hasRemaining()) {
                                System.out.print("Message " + messageNum + "; ");
                                System.out.print("buffer number: " + count++ + " ");
                                System.out.println((char) buffer.get());
                            }
                            buffer.clear();
                        }
                        messageNum++;

                    }
                    if(selectionKey.isConnectable()) {
                        System.out.println("connectable!");
                    }
                } catch (IOException e) {
                    //removing key
                }
            }

        }
    }
}
