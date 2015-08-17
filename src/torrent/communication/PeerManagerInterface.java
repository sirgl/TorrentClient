package torrent.communication;

import torrent.Peer;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public interface PeerManagerInterface {
    Peer getPeer(long id);
    long getPeerId(ByteBuffer name);
    void addPeer(long id);
    void registerPeer(long id, ByteBuffer name);
    void removePeer(long id);
    boolean hasPeer(long id);
}
