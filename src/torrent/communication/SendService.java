package torrent.communication;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public interface SendService {
    void sendMessage(byte[] message, ByteBuffer peerId);
}
